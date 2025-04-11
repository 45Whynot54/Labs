package com.example.labs.data.lab8

import java.math.BigInteger

class EllipticCurve(
    val p: BigInteger,
    val a: BigInteger,
    val b: BigInteger,
    val G: Point,
    val n: BigInteger
) {
    fun multiplyPoint(point: Point, scalar: BigInteger): Point {
        require(scalar >= BigInteger.ZERO) { "Скаляр должен быть неотрицательным" }

        var result = Point.infinity()
        var current = point
        var s = scalar

        while (s > BigInteger.ZERO) {
            if (s.testBit(0)) {
                result = addPoints(result, current)
            }
            current = addPoints(current, current)
            s = s.shiftRight(1)
        }

        return result
    }

    fun addPoints(p1: Point, p2: Point): Point {
        if (p1.isInfinity()) return p2
        if (p2.isInfinity()) return p1

        if (p1.x == p2.x && p1.y == p2.y.negate().mod(p)) {
            return Point.infinity()
        }

        if (p1 == p2) return doublePoint(p1)

        val lambda = (p2.y - p1.y) * (p2.x - p1.x).modInverse(p) % p
        val x3 = (lambda.pow(2) - p1.x - p2.x) % p
        val y3 = (lambda * (p1.x - x3) - p1.y) % p

        return Point(x3, y3)
    }

    private fun doublePoint(point: Point): Point {
        require(!point.isInfinity()) { "Нельзя удваивать точку на бесконечности" }

        val lambda = (BigInteger("3") * point.x.pow(2) + a) *
                (BigInteger.TWO * point.y).modInverse(p) % p
        val x3 = (lambda.pow(2) - BigInteger.TWO * point.x) % p
        val y3 = (lambda * (point.x - x3) - point.y) % p

        return Point(x3, y3)
    }

    data class Point(val x: BigInteger, val y: BigInteger) {
        companion object {
            fun infinity() = Point(BigInteger.ZERO, BigInteger.ZERO)
        }

        fun isInfinity() = x == BigInteger.ZERO && y == BigInteger.ZERO
    }
}