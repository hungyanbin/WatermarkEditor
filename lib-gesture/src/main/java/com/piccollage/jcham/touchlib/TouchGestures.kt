package com.piccollage.jcham.touchlib

import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import java.util.concurrent.TimeUnit


data class CTap(
    val touch: CTouch,         // The first touch of the tap
    val tStart:   Double,      // Time that the first tap happened
    val tPress:   Double?,     // How long the press was
    val rawEvent: CTouchEvent
)

data class CPress(
    val touch: CTouch,
    val tStart: Double,       // Time that the first tap happened
    val tPress: Double,       // How long the press was
    val rawEvent: CTouchEvent
)

data class CMultitap(
    val taps:       List<CTap>
)

fun Observable<CTouchEvent>.dragToPinch(): Observable<CTouchEvent> =
    pairwise()
        .filterMap { (first, second) ->
            if ((first.touches.size == 1) &&
                (second.touches.size == 2)) {
                second
            } else
                null
        }

fun Observable<CTouchEvent>.isDragOrPinch(maxDrag: Double = 10.0): Observable<Boolean> {

    // Produces true for each event corresponds to a drag or pinch or not.
    return pairFirst()
        .map { (eventFirst, event) ->
            if (eventFirst.touches.count() != 1 || event.touches.count() != 1)
                true
            else {
                val t1 = eventFirst.touches[0]
                val t2 = event.touches[0]
                val v = (t1.point - t2.point).abs()
                v.x >= maxDrag || v.y >= maxDrag
            }
        }
}

// ---- Check number of touches, stop passing on events if any has multiple touches
private fun eventTouchFilterer(maxTouches: Int): ((touchEvent: CTouchEvent) -> Boolean) {
    return fun(touchEvent: CTouchEvent): Boolean {
        return touchEvent.touches.count() <= maxTouches
    }
}

fun tapsFromGestures(
    gestures: Observable<Observable<CTouchEvent>>,
    maxDrag: Double = 10.0
): Observable<CTap> {
    return gestures.flatMap { gesture ->

        // Only true at end of gesture if throughout if all no drag or pinch
        val valid = gesture.isDragOrPinch(maxDrag).all{!it}.filter{it}

        // Zip this signal with the first and last events, and from them
        // produce the tap.
        Observables.zip(
            valid.toObservable(),
            gesture.firstElement().toObservable(),
            gesture.lastElement().toObservable()) { _, first, last ->
                CTap(first.touches[0], first.time, last.time - first.time, first)
            }
    }
}

fun pressFromGesture(
    gesture: Observable<CTouchEvent>,
    minTime: Double = 1.0,
    maxDrag: Double = 10.0
): Observable<CPress> {
    val msTime = minTime * 1000.0

    // A drag, or pinch or the end of the gesture will stop the
    // delay timer
    val stop = gesture.isDragOrPinch(maxDrag).filter { it }

    return gesture
        .take(1)
        .delay(msTime.toLong(), TimeUnit.MILLISECONDS)
        .takeUntil(stop)
        .map { event -> CPress(event.touches.first(), event.time, minTime, event) }
}

fun pressesFromGestures(
    gestures: Observable<Observable<CTouchEvent>>,
    minTime: Double = 1.0,
    maxDrag: Double = 10.0
): Observable<CPress> {

    return gestures.flatMap { gesture ->
        pressFromGesture(gesture, minTime = minTime, maxDrag = maxDrag)
    }
}

fun multitapsFromTaps(
    tapsIn: Observable<CTap>,
    maxInterval: Double = 0.3
): Observable<CMultitap> {

    fun multitapScanner(taps: List<CTap>, tap: CTap): List<CTap> =
        if (taps.isEmpty() || tap.tStart - taps.last().tStart > maxInterval)
            listOf(tap)
        else
            taps + tap              // Got a multiple click!

    return tapsIn
            .scan(emptyList(), ::multitapScanner)
            .filter { it.count() >= 1 }
            .map { taps -> CMultitap(taps) }
}

// ---- Convert to extensions

fun Observable<Observable<CTouchEvent>>.taps(maxDrag: Double = 10.0)
    = tapsFromGestures(this, maxDrag)

//fun Observable<Observable<CTouchEvent>>.presses(
//    minTime: Double = 1.0,
//    maxDrag: Double = 10.0)
//    = pressesFromGestures(this, minTime, maxDrag)

fun Observable<CTouchEvent>.press(
    minTime: Double = 1.0,
    maxDrag: Double = 10.0)
    = pressFromGesture(this, minTime, maxDrag)

//fun Observable<CTap>.multitaps(maxInterval: Double = 0.3)
//    = multitapsFromTaps(this, maxInterval)

fun Observable<Observable<CTouchEvent>>.multitaps(maxDrag: Double = 10.0, maxInterval: Double = 0.3)
    = multitapsFromTaps(tapsFromGestures(this, maxDrag), maxInterval)