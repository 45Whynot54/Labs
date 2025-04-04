package com.example.labs.data.lab8

data class Point(val x: Int, val y: Int) {
    val isInfinity: Boolean = (x == Int.MAX_VALUE && y == Int.MAX_VALUE)
    companion object {
        val INFINITY = Point(Int.MAX_VALUE, Int.MAX_VALUE)
    }

    override fun toString(): String =
        if (isInfinity) "O(INF)" else "($x, $y)"
}