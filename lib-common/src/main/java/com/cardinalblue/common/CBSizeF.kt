package com.cardinalblue.common

/**
 * Same structure with `CBPointF`, but different meaning and usage.
 * Size is use to describe the width and height of an object.
 */
data class CBSizeF(val width: Float = 0f, val height: Float = 0f) {

    constructor(x1: Double, y1: Double): this(x1.toFloat(), y1.toFloat())

    override fun toString(): String = "($width, $height)"
    operator fun unaryMinus()       = CBSizeF(-width, -height)
    operator fun plus(p: CBSizeF)   = CBSizeF(width + p.width, height + p.height)
    operator fun minus(p: CBSizeF)  = CBSizeF(width - p.width, height - p.height)
    operator fun times(s: Float)    = CBSizeF(width * s, height * s)
    operator fun div(s: Float)      = CBSizeF(width / s, height / s)

    // Transformation
    fun scale(scale: Float)   = CBSizeF(width * scale, height * scale)
    fun scale(scale: Double)  = CBSizeF(width * scale, height * scale)
    fun unscale(scale: Float) = CBSizeF(width / scale, height / scale)
    fun scale(scaleX: Float, scaleY: Float) = CBSizeF(width * scaleX, height * scaleY)

    // Comparison
    private val precision = 0.000001
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this.javaClass != other.javaClass) return false
        other as CBSizeF
        if (Math.abs(width - other.width) > precision ||
            Math.abs(height - other.height) > precision) return false
        return true
    }
}
