package com.cardinalblue.common

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.PI

class CBPositioningTest {

    /**
     * ^
     * |
     * +-------X (4, 3)
     * |       |
     * |       |
     * +-------+----->
     */
    private val rotationInRadius = Math.acos((4.0 / 5.0)).toFloat()
    private val rotationInRadiusPI_2 = (PI / 2).toFloat()
    private val rotationInRadiusPI_4 = (PI / 4).toFloat()
    private val twoRootSquare = 1.4142135f

    @Test
    fun `Apply a simple transform by chain`() {
        var p = CBPositioning()
        var transform = CBPositioning(CBPointF(5f, 5f))

        p = p.chain(transform)

        assertEquals(p, transform)
    }


    @Test
    fun `Apply a transform to a rotated point`() {
        var p = CBPositioning(CBPointF(5f, 5f), rotationInRadiusPI_4)
        var transform = CBPositioning(CBPointF(0f, twoRootSquare))

        p = p.chain(transform)

        assertEquals(CBPositioning(CBPointF(4f, 6f), rotationInRadiusPI_4), p)
    }

    @Test
    fun `Apply a transform to a rotated point2`() {
        val p = CBPositioning(CBPointF(0f, 0f),
                              rotationInRadiusPI_4,
                              1f,
                              0)

        val pMove = CBPositioning(CBPointF(2f, 0f), rotationInRadiusPI_2)

        val delta = p.chain(pMove)

        assertEquals(CBPositioning(CBPointF(1.4142135f, 1.4142135f), 3 * rotationInRadiusPI_4), delta)
    }

    @Test
    fun `Chain and relative`() {
        val p1 = CBPositioning(CBPointF(0f, 0f),
                               0f,
                               1f,
                               0)

        val p2 = CBPositioning(CBPointF(2f, 0f), rotationInRadiusPI_2)

        val formula = p1.chain(p2.relative(p1))

        assertEquals(p2, formula)
    }

    @Test
    fun `Relative functionality`() {
        val p = CBPositioning(CBPointF(5f, 5f), rotationInRadius)
        val p2 = CBPositioning(CBPointF(9f, 8f), rotationInRadius)

        val delta = p2.relative(p)

        assertEquals(delta, CBPositioning(CBPointF(5f, 0f)))
    }

    /**
     * ^
     * |
     * |       X (4, 3)
     * |     -f
     * |  -
     * |-
     * +----------X----->
     *            (5,0)
     */
    @Test
    fun `Rotate a point`() {
        var p = CBPointF(5f, 0f)
        p = p.rotate(rotationInRadius)

        assertEquals(p, CBPointF(4f, 3f))
    }


}