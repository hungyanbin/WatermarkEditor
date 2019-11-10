package com.piccollage.jcham.touchlib
import com.cardinalblue.common.CBPointF
import com.cardinalblue.common.CBTransform
import io.reactivex.Observable

open class TouchTransform(val move: Point = Point(0.0, 0.0),
                          val rotate: Double = 0.0,
                          val scale: Double = 1.0,
                          var event1: CTouchEvent = CTouchEvent(0.0, arrayListOf()),
                          var event2: CTouchEvent = CTouchEvent(0.0, arrayListOf()),
                          val touchCount: Int = 1) {

    operator fun plus(t: TouchTransform) =
        TouchTransform(
            move.rotate(t.rotate).scale(t.scale) + t.move,
            rotate + t.rotate,
            scale * t.scale)

    fun repivot(pivot1: Point, pivot2: Point): TouchTransform {
        val np = pivot1 + move
        val d1 = pivot2 - pivot1
        val d2 = d1.rotate(rotate).scale(scale)
        return TouchTransform(np + d2 - pivot2,
                              rotate, scale)
    }

    fun add(other: TouchTransform): TouchTransform {
        return TouchTransform(move = this.move + other.move,
                              rotate = this.rotate + other.rotate,
                              scale = this.scale * other.scale)
    }

    fun replace(other: TouchTransform) =
        TouchTransform(
            move = Point(x = if (other.move.x == 0.0) this.move.x else other.move.x,
                         y = if (other.move.y == 0.0) this.move.y else other.move.y),
            rotate = if (other.rotate == 0.0) this.rotate else other.rotate,
            scale = if (other.scale == 1.0) this.scale else other.scale
                      )
}

fun calculateTransformFromVectors(pivot: Point,
                                          vector1: Vector,
                                          vector2: Vector
): TouchTransform
{
    // Calculate scale and rotate
    val rotate = vector1.angleTo(vector2)
    val scale  = vector1.scaleTo(vector2)

    // Calculate move
    val vp = pivot - vector1.p1                 // Vector to pivot
    val vd = vp.rotate(rotate).scale(scale)     // Transformed vector to pivot
    val p2 = vector2.p1 + vd                    // New pivot
    val move = p2 - pivot

    return TouchTransform(move, rotate, scale, touchCount = 2)
}

fun calculateTransformFromEvents(pivot: Point,
                                 event1: CTouchEvent,
                                 event2: CTouchEvent
): TouchTransform {
    val touches1 = event1.touches.groupBy { it.id }
    val touches2 = event2.touches.groupBy { it.id }
    val common =  touches1.keys.intersect(touches2.keys)

    return when(common.count()) {
        1 -> {
            val touch1 = touches1[common.first()]!!.first()
            val touch2 = touches2[common.first()]!!.first()
            val move = touch2.point - touch1.point
            TouchTransform(event1 = event1, event2 = event2, move = move)
        }
        2 -> {
            val k1 = common.first()
            val k2 = common.last()
            val vector1 = Vector(
                touches1[k1]!!.first().point,
                touches1[k2]!!.first().point
            )
            val vector2 = Vector(
                touches2[k1]!!.first().point,
                touches2[k2]!!.first().point
            )
            val transform = calculateTransformFromVectors(pivot, vector1, vector2)
            transform.event1 = event1
            transform.event2 = event2
            transform
        }
        else -> TouchTransform(touchCount = common.count())
    }

}

fun transformsFromGesture(gesture: Observable<CTouchEvent>, pivot: Point = Point()): Observable<TouchTransform> =
    gesture
        .pairwise()
        .map { events -> calculateTransformFromEvents(pivot, events.first, events.second) }

fun Observable<Observable<CTouchEvent>>.transforms(pivot: Point = Point(0.0, 0.0)): Observable<TouchTransform> {
    return switchMap { gesture ->
        gesture
            .pairwise()
            .map { events -> calculateTransformFromEvents(pivot, events.first, events.second) }
    }
}

fun TouchTransform.toCBTransform(): CBTransform =
    CBTransform(move = CBPointF(this.move.x, this.move.y),
                rotate = this.rotate.toFloat(),
                scale = this.scale.toFloat(),
                z = 0)