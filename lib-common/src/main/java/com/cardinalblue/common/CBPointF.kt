package com.cardinalblue.common

/**
 * Same structure with `CBSizeF`, but different meaning and usage.
 * Point is use to describe the position of an object.
 */
data class CBPointF(val x: Float = 0f, val y: Float = 0f) {

    constructor(x1: Double, y1: Double): this(x1.toFloat(), y1.toFloat())

    override fun toString(): String = "($x, $y)"
    operator fun unaryMinus()       = CBPointF(-x, -y)
    operator fun plus(p: CBPointF)     = CBPointF(x + p.x, y + p.y)
    operator fun minus(p: CBPointF)    = CBPointF(x - p.x, y - p.y)
    operator fun times(s: Float)   = CBPointF(x * s, y * s)
    operator fun div(s: Float)     = CBPointF(x / s, y / s)

    fun abs() = CBPointF(kotlin.math.abs(x), kotlin.math.abs(y))
    fun magnitude2(): Float = x * x + y * y

    // Transformation
    fun rotate(angle: Float): CBPointF =
        CBPointF(
            x * Math.cos(angle.toDouble()) - y * Math.sin(angle.toDouble()),
            x * Math.sin(angle.toDouble()) + y * Math.cos(angle.toDouble())
        )

    fun rotateDegree(angle: Float): CBPointF =
        rotate(Math.toRadians(angle.toDouble()).toFloat())

    fun rotateDegreeInverse(angle: Float): CBPointF = rotateDegree(-1 * angle)
    fun rotateInverse(angle: Float): CBPointF = rotate(-1 * angle)

    fun scale(scale: Float): CBPointF = CBPointF(x * scale, y * scale)
    fun scale(scale: Double): CBPointF = CBPointF(x * scale, y * scale)
    fun scale(scaleX: Float, scaleY: Float): CBPointF = CBPointF(x * scaleX, y * scaleY)
    fun scale(scale: CBPointF): CBPointF = CBPointF(x * scale.x, y * scale.y)
    fun unscale(scale: Float): CBPointF = CBPointF(x / scale, y / scale)
    fun unscale(scale: CBPointF): CBPointF = CBPointF(x / scale.x, y / scale.y)

    // Comparison
    private val precision = 0.000001
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this.javaClass != other.javaClass) return false
        other as CBPointF
        if (Math.abs(x - other.x) > precision ||
            Math.abs(y - other.y) > precision) return false
        return true
    }

    fun isNotMoved(): Boolean {
        return x == 0f && y == 0f
    }
}
