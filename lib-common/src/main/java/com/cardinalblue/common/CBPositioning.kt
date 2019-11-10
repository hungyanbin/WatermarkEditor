package com.cardinalblue.common

typealias ZType = Int

data class CBPositioning(
    val point: CBPointF = CBPointF(0f, 0f),
    val rotateInRadians: Float = 0.0f,
    val scale: Float = 1.0f,
    val z: ZType = 0) {

    constructor(x: Float, y: Float, rotate: Float, scale: Float, z: Int) : this(CBPointF(x, y), rotate, scale, z)
    constructor(x: Float, y: Float, rotate: Float, scale: Float) : this(CBPointF(x, y), rotate, scale, 0)

    var rotateInDegree: Float = rotateInRadians.toDegree()

    infix fun or(other: CBPositioning) =
        CBPositioning(
            point = if (point.isNotMoved()) other.point else point,
            rotateInRadians = if (rotateInRadians == 0f) other.rotateInRadians else rotateInRadians,
            scale = if (scale == 1f) other.scale else scale,
            z = if (z == 0) other.z else z)

    fun transform(transform: CBTransform) =
        CBPositioning(
            point + transform.move,
            rotateInRadians + transform.rotate,
            scale * transform.scale,
            z + transform.z)

    fun invert(): CBPositioning =
        CBPositioning(
            -point,
            -rotateInRadians,
            1/scale,
            -z)

    // Given the relative position of other,
    // get the absolute position.
    fun chain(next: CBPositioning): CBPositioning =
        CBPositioning(
            point = point.plus(next.point.rotate(rotateInRadians).scale(scale)),
            rotateInRadians = rotateInRadians.plus(next.rotateInRadians),
            scale = scale.times(next.scale),
            z = z + next.z)

    // Given the absolute position of self and other position, get the
    // relative position of self to the other.
    fun relative(other: CBPositioning) =
        CBPositioning(
            point = point.minus(
                other.point).rotateInverse(other.rotateInRadians)
                .unscale(other.scale),
            rotateInRadians = rotateInRadians.minus(other.rotateInRadians),
            scale = scale.div(other.scale),
            z = z - other.z)

    fun replace(other: CBPositioning): CBPositioning =
        CBPositioning(
            CBPointF(
                x = if (other.point.x == 0f) point.x else other.point.x,
                y = if (other.point.y == 0f) point.y else other.point.y
                    ),
            rotateInRadians = if (other.rotateInRadians == 0f) rotateInRadians else other.rotateInRadians,
            scale = if (other.scale == 1f) scale else other.scale,
            z = if (other.z == 0) z else other.z)

    fun copyWithNewX(x: Float) =
        this.copy(point = CBPointF(x = x, y = this.point.y))

    fun copyWithNewY(y: Float) =
        this.copy(point = CBPointF(x = this.point.x, y = y))
}

fun Float.toDegree(): Float = Math.toDegrees(this.toDouble()).toFloat()
fun Float.toRadians(): Float = Math.toRadians(this.toDouble()).toFloat()