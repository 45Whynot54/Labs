package com.example.labs.data.lab8

object EllipticCurveUtils {
    // Проверка принадлежности точки кривой
    fun isPointOnCurve(a: Int, b: Int, p: Int, point: Point): Boolean {
        if (point.isInfinity) return true
        val (x, y) = point
        val lhs = (y * y) % p
        val rhs = (x * x * x + a * x + b) % p
        return lhs == rhs
    }

    // Сложение двух точек
    fun addPoints(a: Int, p: Int, P: Point, Q: Point): Point {
        if (P.isInfinity) return Q
        if (Q.isInfinity) return P
        if (P.x == Q.x && (P.y + Q.y) % p == 0) return Point.INFINITY

        val lambda = if (P != Q) {
            val numerator = (Q.y - P.y).mod(p)
            val denominator = (Q.x - P.x).mod(p)
            (numerator * modularInverse(denominator, p)).mod(p)
        } else {
            val numerator = (3 * P.x * P.x + a).mod(p)
            val denominator = (2 * P.y).mod(p)
            (numerator * modularInverse(denominator, p)).mod(p)
        }

        val x3 = (lambda * lambda - P.x - Q.x).mod(p)
        val y3 = (lambda * (P.x - x3) - P.y).mod(p)
        return Point(x3, y3)
    }

    // Умножение точки на скаляр (Double-and-Add алгоритм)
    fun multiplyPoint(a: Int, p: Int, point: Point, scalar: Int): Point {
        var result = Point.INFINITY
        var current = point
        var n = scalar
        while (n > 0) {
            if (n and 1 == 1) {
                result = addPoints(a, p, result, current)
            }
            current = addPoints(a, p, current, current)
            n = n shr 1
        }
        return result
    }

    // Обратный элемент по модулю
    private fun modularInverse(num: Int, mod: Int): Int {
        require(num != 0) { "Деление на ноль" }
        var t = 0
        var newT = 1
        var r = mod
        var newR = num
        while (newR != 0) {
            val quotient = r / newR
            val temp = newT
            newT = t - quotient * newT
            t = temp
            val tempR = newR
            newR = r - quotient * newR
            r = tempR
        }
        if (r > 1) throw ArithmeticException("Обратный элемент не существует")
        return t.mod(mod)
    }
}