package com.example.labs.data.lab4

import com.example.labs.domain.lab4.RSA
import java.math.BigInteger

object RSAImpl : RSA {

    override fun encrypt(message: String, publicKey: Pair<BigInteger, BigInteger>): String {
        val (n, e) = publicKey
        val m = BigInteger(message.toByteArray(Charsets.UTF_8))
        val ciphertext = m.modPow(e, n) // Шифрование: c = m^e mod n
        return ciphertext.toString(16) // Возвращаем зашифрованное сообщение в виде HEX-строки
    }

    override fun decrypt(ciphertext: String, privateKey: Pair<BigInteger, BigInteger>): String {
        val (n, d) = privateKey
        val m = BigInteger(ciphertext, 16) // Преобразуем HEX-строку в BigInteger
        val message = m.modPow(d, n) // Расшифрование: m = c^d mod n
        return String(message.toByteArray(), Charsets.UTF_8) // Возвращаем расшифрованное сообщение
    }
}