package com.example.labs.data.lab4

import com.example.labs.domain.lab4.RSA
import java.math.BigInteger

object RSAImpl : RSA {

    override fun encrypt(message: String, publicKey: Pair<BigInteger, BigInteger>): String {
        val (n, e) = publicKey
        val m = BigInteger(message.toByteArray(Charsets.UTF_8))
        val ciphertext = m.modPow(e, n)
        return ciphertext.toString(16)
    }

    override fun decrypt(ciphertext: String, privateKey: Pair<BigInteger, BigInteger>): String {
        val (n, d) = privateKey
        val m = BigInteger(ciphertext, 16)
        val message = m.modPow(d, n)
        return String(message.toByteArray(), Charsets.UTF_8)
    }
}