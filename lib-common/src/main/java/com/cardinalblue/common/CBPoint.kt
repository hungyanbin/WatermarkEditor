package com.cardinalblue.common

open class CBPoint(val x: Double = 0.0, val y: Double = 0.0) {
    override fun toString(): String = "($x, $y)"

    operator fun unaryMinus()       = CBPoint(-x, -y)
    operator fun plus(p: CBPoint)     = CBPoint(x + p.x, y + p.y)
    operator fun minus(p: CBPoint)    = CBPoint(x - p.x, y - p.y)
    operator fun times(s: Double)   = CBPoint(x * s, y * s)
    operator fun div(s: Double)     = CBPoint(x / s, y / s)

    fun abs()        = CBPoint(kotlin.math.abs(x), kotlin.math.abs(y))
    fun magnitude2() = x * x + y * y

    // Comparison
    private val precision = 0.000001
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this.javaClass != other.javaClass) return false
        other as CBPoint
        if (Math.abs(x - other.x) > precision &&
            Math.abs(y - other.y) > precision) return false
        return true
    }

    // Transformation
    fun rotate(angle: Double): CBPoint =
        CBPoint(
            x * Math.cos(angle) - y * Math.sin(angle),
            x * Math.sin(angle) + y * Math.cos(angle)
               )
    fun scale(scale: Double): CBPoint =
        CBPoint(x * scale, y * scale)
    fun scale(scaleX: Double, scaleY: Double): CBPoint =
        CBPoint(x * scaleX, y * scaleY)
    fun scale(scale: CBPoint): CBPoint =
        CBPoint(x * scale.x, y * scale.y)
    fun unscale(scale: CBPoint): CBPoint =
        CBPoint(x / scale.x, y / scale.y)
}