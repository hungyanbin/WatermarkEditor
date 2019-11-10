package com.cardinalblue.common

data class CBRect(val left: Int, val top: Int, val right: Int, val bottom: Int){
    val width: Int
        get() = right - left
    val height: Int
        get() = bottom - top

    fun isEmpty() = left >= right || top >= bottom

    fun contains(x: Int, y: Int): Boolean {
        return (left < right && top < bottom  // check for empty first
            && x >= left && x < right && y >= top && y < bottom)
    }

    companion object {
        val NullRect = CBRect(0, 0, 0, 0)
    }
}

data class CBRectF(val left: Float, val top: Float, val right: Float, val bottom: Float){
    companion object {
        val EMPTY = CBRectF(0F, 0F, 0F, 0F)
    }

    val width: Float
        get() = right - left
    val height: Float
        get() = bottom - top
    fun empty() = width == 0f && height == 0f

    fun isEmpty() = left >= right || top >= bottom

    fun contains(x: Float, y: Float): Boolean {
        return (left < right && top < bottom  // check for empty first
            && x >= left && x < right && y >= top && y < bottom)
    }

    fun centerX(): Float {
        return (left + right) * 0.5f
    }

    fun centerY(): Float {
        return (top + bottom) * 0.5f
    }

    fun inset(dx: Float, dy: Float): CBRectF {
        val newLeft = left + dx
        val newTop = top + dy
        val newRight = right - dx
        val newBottom = bottom - dy

        return CBRectF(newLeft, newTop, newRight, newBottom)
    }

    /**
     * Offset the rectangle by adding dx to its left and right coordinates, and
     * adding dy to its top and bottom coordinates.
     *
     * @param dx The amount to add to the rectangle's left and right coordinates
     * @param dy The amount to add to the rectangle's top and bottom coordinates
     */
    fun offset(dx: Float, dy: Float): CBRectF {
        return CBRectF(left + dx, top + dy, right + dx, bottom + dy)
    }
}