package com.example.labs.data.lab8

data class ECParameters(
    val a: Int,
    val b: Int,
    val p: Int,
    val basePoint: Point,
    val privateKeyA: Int,
    val privateKeyB: Int
) {
    init {
        require(isValidCurve()) { "Кривая не удовлетворяет условию 4a³ + 27b² ≠ 0 mod p" }
        require(EllipticCurveUtils.isPointOnCurve(a, b, p, basePoint)) {
            "Базовая точка не принадлежит кривой"
        }
    }

    private fun isValidCurve(): Boolean {
        val term1 = (4 * a * a * a) % p
        val term2 = (27 * b * b) % p
        return (term1 + term2) % p != 0
    }
}