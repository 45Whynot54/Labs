package com.example.labs.data.lab8

object DiffieHellmanECDH {
    // Генерация публичного ключа
    fun generatePublicKey(params: ECParameters, isUserA: Boolean): Point {
        val privateKey = if (isUserA) params.privateKeyA else params.privateKeyB
        return EllipticCurveUtils.multiplyPoint(
            params.a,
            params.p,
            params.basePoint,
            privateKey
        )
    }

    // Вычисление общего секрета
    fun computeSharedSecret(params: ECParameters, isUserA: Boolean, publicKey: Point): Point {
        val privateKey = if (isUserA) params.privateKeyA else params.privateKeyB
        return EllipticCurveUtils.multiplyPoint(
            params.a,
            params.p,
            publicKey,
            privateKey
        )
    }
}