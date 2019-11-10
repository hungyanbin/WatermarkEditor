package com.yanbin.watermarkeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.withMatrix
import com.piccollage.jcham.touchlib.*
import io.reactivex.Observable

@SuppressLint("AppCompatCustomView")
class WaterMarkView : ImageView {

    private var waterMarkImage: Bitmap? = null
    private val renderMatrix = Matrix()

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    @SuppressLint("CheckResult")
    fun setWaterMark(bitmap: Bitmap) {
        waterMarkImage = bitmap

        val gestureObservable = CBTouch.gesturesFromView(this).share()
        val pivot = Point()

        //pinch
        gestureObservable
            .flatMap { gesture -> toPinchTransform(gesture, pivot) }
            .subscribe { transform ->
                updateRenderMatrix(transform)
                invalidate()
            }
    }

    private fun updateRenderMatrix(transform: TouchTransform) {
        val rotationDegree = transform.rotate.toFloat().toDegree()
        renderMatrix.postTranslate(transform.move.x.toFloat(), transform.move.y.toFloat())
        renderMatrix.postRotate(rotationDegree)
        renderMatrix.postScale(transform.scale.toFloat(), transform.scale.toFloat())
    }

    private fun Float.toDegree(): Float = Math.toDegrees(this.toDouble()).toFloat()

    /**
     *   After switch to drag, downStream will be noticed that it is completed
     *
     *   - : one finger
     *   + : two fingers
     *   | : finished
     *
     *   UpStream
     *   - - - - + + + + + - - - |
     *
     *   DownStream
     *           + + + + + |
     */
    private fun toPinchTransform(gesture: Observable<CTouchEvent>, pivot: Point): Observable<TouchTransform> {
        return gesture
            .pairwise()
            .skipWhile { events -> !(events.first.touches.size == 2 && events.second.touches.size == 2) }
            .takeWhile { events -> events.first.touches.size == 2 && events.second.touches.size == 2 }
            .map { events -> calculateTransformFromEvents(pivot, events.first, events.second) }
    }

    fun getWaterMarkMatrix(): Matrix {
        val waterMarkMatrix = Matrix()
        val invertImageMatrix = Matrix()
        imageMatrix.invert(invertImageMatrix)
        waterMarkMatrix.preConcat(renderMatrix)
        waterMarkMatrix.preConcat(imageMatrix)
        waterMarkMatrix.postConcat(invertImageMatrix)
        return waterMarkMatrix
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        waterMarkImage?.let { image ->
            canvas.withMatrix(renderMatrix) {
                canvas.drawBitmap(image, imageMatrix, null)
            }
        }
    }

}