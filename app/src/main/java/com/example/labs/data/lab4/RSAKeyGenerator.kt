package com.example.labs.data.lab4

import java.math.BigInteger
import java.util.Random

object RSAKeyGenerator {
    private fun generatePrime(bitLength: Int): BigInteger {
        return BigInteger.probablePrime(bitLength, Random())
    }

    // Генерация ключей
    fun generateKeys(bitLength: Int): Triple<BigInteger, BigInteger, BigInteger> {
        // Генерация двух простых чисел
        val p = generatePrime(bitLength)
        val q = generatePrime(bitLength)

        // Вычисление n и φ(n)
        val n = p.multiply(q)
        val phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))

        // Выбор открытого ключа e (обычно 65537)
        val e = generateRandomE(phi)

        // Вычисление закрытого ключа d
        val d = e.modInverse(phi)

        return Triple(n, e, d)
    }

    private fun generateRandomE(phi: BigInteger): BigInteger {
        val random = Random()
        var e: BigInteger

        do {
            // Генерируем случайное число e, меньшее φ(n)
            e = BigInteger(phi.bitLength(), random)
        } while (e >= phi || e.gcd(phi) != BigInteger.ONE) // Проверяем, что e < φ(n) и gcd(e, φ(n)) = 1

        return e
    }
}