package com.example.labs.data.lab8

import java.math.BigInteger
import java.security.SecureRandom


class ECDHKeyExchange(private val curve: EllipticCurve) {

    var privateKey: BigInteger? = null
    var publicKey: EllipticCurve.Point? = null
        private set

    fun generateKeys() {
        privateKey = generatePrivateKey()
        publicKey = curve.multiplyPoint(curve.G, privateKey!!)
    }

    fun computeSharedSecret(otherPublicKey: EllipticCurve.Point): EllipticCurve.Point {
        require(privateKey != null) { "Private key not generated" }
        return curve.multiplyPoint(otherPublicKey, privateKey!!)
    }

    private fun generatePrivateKey(): BigInteger {
        val secureRandom = SecureRandom()
        return BigInteger(curve.n.bitLength(), secureRandom)
            .mod(curve.n.subtract(BigInteger.ONE))
            .add(BigInteger.ONE)
    }
}