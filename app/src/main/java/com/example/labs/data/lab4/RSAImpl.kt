package com.example.labs.data.lab4

import com.example.labs.domain.lab4.RSA
import java.math.BigInteger

object RSAImpl : RSA {

    override fun encrypt(message: String, publicKey: Pair<String, String>): String {
        val n = BigInteger(publicKey.first, 16)
        val e = BigInteger(publicKey.second, 16)
        val m = BigInteger(message.toByteArray(Charsets.UTF_8))
        val ciphertext = m.modPow(e, n)
        return ciphertext.toString(16)
    }

    override fun decrypt(ciphertext: String, privateKey: Pair<String, String>): String {
        val n = BigInteger(privateKey.first, 16)
        val d = BigInteger(privateKey.second, 16)
        val m = BigInteger(ciphertext, 16)
        val message = m.modPow(d, n)
        return String(message.toByteArray(), Charsets.UTF_8)
    }
}