package com.example.labs.data.lab4

import com.example.labs.domain.lab4.RSA
import java.math.BigInteger
import java.util.Base64

object RSAImpl : RSA {

    override fun encrypt(message: String, publicKey: Pair<String, String>): String {
        val n = BigInteger(publicKey.first, 16)
        val e = BigInteger(publicKey.second, 16)

        val messageBytes = message.toByteArray(Charsets.UTF_8)

        val m = BigInteger(1, messageBytes)
        val ciphertext = m.modPow(e, n)
        return Base64.getEncoder().encodeToString(ciphertext.toByteArray())
    }

    override fun decrypt(ciphertext: String, privateKey: Pair<String, String>): String {
        val n = BigInteger(privateKey.first, 16)
        val d = BigInteger(privateKey.second, 16)

        val ciphertextBytes = Base64.getDecoder().decode(ciphertext)
        val m = BigInteger(1, ciphertextBytes)
        val messageBytes = m.modPow(d, n).toByteArray()

        val startIndex = if (messageBytes[0] == 0.toByte()) 1 else 0
        return String(messageBytes, startIndex, messageBytes.size - startIndex, Charsets.UTF_8)
    }
}