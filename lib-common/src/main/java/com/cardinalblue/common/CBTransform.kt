package com.cardinalblue.common

data class CBTransform(val move:    CBPointF   = CBPointF(0.0f, 0.0f),
                       val rotate:  Float      = 0.0f,
                       val scale:   Float      = 1.0f,
                       val z:       Int        = 0) {

    // A Transform consists of a displacement/move and *then* a rotation
    // about an implied pivot point.

    // Note that this assumes the same pivot
    operator fun plus(t: CBTransform) =
        CBTransform(
            move.rotate(t.rotate).scale(t.scale) + t.move,
            rotate + t.rotate,
            scale * t.scale,
            z + t.z
                 )

    fun repivot(pivot1: CBPointF, pivot2: CBPointF): CBTransform {
        val np = pivot1 + move
        val d1 = pivot2 - pivot1
        val d2 = d1.rotate(rotate).scale(scale)
        return CBTransform(np + d2 - pivot2,
                         rotate, scale)
    }

}