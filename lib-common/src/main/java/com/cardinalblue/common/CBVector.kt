package com.cardinalblue.common

class CBVector(val p1: CBPointF, val p2: CBPointF) {

    fun toPoint(): CBPointF = p2 - p1

    fun magnitude2() = toPoint().magnitude2()

    fun angleTo(other: CBVector): Double {
        val vn1 = this .toPoint()
        val vn2 = other.toPoint()
        return (Math.atan2(vn2.y.toDouble(), vn2.x.toDouble()) -
                Math.atan2(vn1.y.toDouble(), vn1.x.toDouble()))
    }

    fun scaleTo(other: CBVector): Double =
        Math.sqrt(
            other.toPoint().magnitude2().toDouble() /
            this. toPoint().magnitude2().toDouble())
}