package com.yanbin.watermarkeditor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.withMatrix

@SuppressLint("AppCompatCustomView")
class WaterMarkView : ImageView {

    private var waterMarkImage: Bitmap? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    fun setWaterMark(bitmap: Bitmap) {
        waterMarkImage = bitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.withMatrix(imageMatrix) {
            waterMarkImage?.let {
                canvas.drawBitmap(it, width / 2f, height / 2f, null)
            }
        }
    }
}