package com.piccollage.jcham.touchlib

open class Point(val x: Double = 0.0, val y: Double = 0.0) {
    operator fun plus(p: Point): Point {
        return Point(x + p.x, y + p.y)
    }
    operator fun minus(p: Point): Point {
        return Point(x - p.x, y - p.y)
    }
    fun abs(): Point {
        return Point(kotlin.math.abs(x), kotlin.math.abs(y))
    }
    fun magnitude2(): Double {
        return x * x + y * y
    }

    // Comparison
    internal val precision = 0.000001
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (this.javaClass != other.javaClass) return false
        other as Point
        if (Math.abs(x - other.x) > precision &&
            Math.abs(y - other.y) > precision) return false
        return true
    }

    // Transformation
    fun rotate(angle: Double): Point =
        Point(
            x * Math.cos(angle) - y * Math.sin(angle),
            x * Math.sin(angle) + y * Math.cos(angle)
        )
    fun scale(scale: Double): Point =
        Point(x * scale, y * scale)
}

data class Vector(val p1: Point, val p2: Point) {

    private fun toPoint(): Point = p2 - p1
    fun magnitude2() = toPoint().magnitude2()

    fun angleTo(other: Vector): Double {
        val vn1 = this .toPoint()
        val vn2 = other.toPoint()
        return (Math.atan2(vn2.y, vn2.x) - Math.atan2(vn1.y, vn1.x))
    }
    fun scaleTo(other: Vector): Double =
        Math.sqrt(
            other.toPoint().magnitude2() /
            this. toPoint().magnitude2()
            )
}