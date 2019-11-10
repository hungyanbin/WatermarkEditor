package com.piccollage.jcham.touchlib

import android.view.*
import com.jakewharton.rxbinding2.view.RxView

import io.reactivex.*
import io.reactivex.subjects.*

//typealias Timeunit = Double

data class CTouch(val id: Int, val point: Point, val targets: List<CTouchTarget> = emptyList())

interface CTouchTarget

//typealias CTouchGesture = Observable<CTouchEvent>

object CBTouch {

    fun gesturesFromView(view: View): Observable<Observable<CTouchEvent>> =
        gesturesFromMotionEvents(RxView.touches(view))

    fun gesturesFromMotionEvents(source: Observable<MotionEvent>): Observable<Observable<CTouchEvent>> {

        fun touchEventFromMotionEvent(motionEvent: MotionEvent): CTouchEvent? {
            if (motionEvent.pointerCount == 0) return null
            val touches = (0 until motionEvent.pointerCount).map { i ->
                CTouch(
                    motionEvent.getPointerId(i),
                    Point(
                        motionEvent.getX(i).toDouble(),
                        motionEvent.getY(i).toDouble()
                    )
                )
            }
            return CTouchEvent(motionEvent.eventTime.toDouble() / 1000.0, touches)
        }
        data class ScannerState(val curGesture: Subject<CTouchEvent>? = null)
        fun scanner(state: ScannerState, motionEvent: MotionEvent): ScannerState {
            // println(">>>> MotionEvent ${motionEvent}")
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // New gesture!
                    val newGesture = ReplaySubject.create<CTouchEvent>()
                    val touchEvent = touchEventFromMotionEvent(motionEvent)
                    if (touchEvent != null)
                        newGesture?.onNext(touchEvent)
                    return ScannerState(newGesture)
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    if (state.curGesture != null) {

                        // If there's touch information send it
                        if (motionEvent.pointerCount > 0) {
                            val touchEvent = touchEventFromMotionEvent(motionEvent)
                            if (touchEvent != null)
                                state.curGesture.onNext(touchEvent)
                        }

                        // Gesture finished, reset
                        state.curGesture.onComplete()
                    }
                }
                MotionEvent.ACTION_MOVE,
                MotionEvent.ACTION_POINTER_UP,
                MotionEvent.ACTION_POINTER_DOWN -> {
                    // Gesture in progress
                    val touchEvent = touchEventFromMotionEvent(motionEvent)
                    if (touchEvent != null)
                        state.curGesture?.onNext(touchEvent)
                }
            }
            return state
        }

        return source
            .scan(ScannerState(), ::scanner)
            .filter { it.curGesture != null }
            .map { it.curGesture as Observable<CTouchEvent> }
            .distinctUntilChanged()
    }
}
fun Observable<MotionEvent>.gestures() = CBTouch.gesturesFromMotionEvents(this)
fun View.gestures()                    = CBTouch.gesturesFromView(this)