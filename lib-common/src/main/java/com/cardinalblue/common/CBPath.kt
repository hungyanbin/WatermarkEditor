package com.cardinalblue.common

class CBPath(val points: List<CBPointF>) {

    fun isEmpty() = points.isEmpty()

    fun copy(): CBPath {
        return CBPath(ArrayList(points))
    }

    companion object {
        val INVALID_PATH = CBPath(emptyList())
    }
}