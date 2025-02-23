package com.example.labs.data.lab4

import com.example.labs.domain.lab4.RSA
import java.math.BigInteger

object RSAImpl : RSA {

    // Параметры RSA (n, e, d)
    private lateinit var n: BigInteger
    private lateinit var e: BigInteger
    private lateinit var d: BigInteger

    // Инициализация параметров RSA
    fun initialize(n: BigInteger, e: BigInteger, d: BigInteger) {
        this.n = n
        this.e = e
        this.d = d
    }

    // Шифрование
    override fun encrypt(message: String, publicKey: Pair<BigInteger, BigInteger>): String {
        val (n, e) = publicKey
        val m = BigInteger(message.toByteArray())
        val ciphertext = m.modPow(e, n)
        return ciphertext.toString(16)
    }

    // Расшифрование
    override fun decrypt(ciphertext: String): String {
        val m = BigInteger(ciphertext, 16)
        val message = m.modPow(d, n)
        return String(message.toByteArray())
    }
}