package com.example.labs.data.lab4

import java.math.BigInteger
import java.util.Random

object RSAKeyGenerator {
    private fun generatePrime(bitLength: Int): BigInteger {
        return BigInteger.probablePrime(bitLength, Random())
    }

    // Генерация ключей
    fun generateKeys(bitLength: Int): Triple<BigInteger, BigInteger, BigInteger> {

        val p = generatePrime(bitLength)
        val q = generatePrime(bitLength)

        val n = p.multiply(q)
        val phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
        val e = generateRandomE(phi)
        val d = e.modInverse(phi)

        return Triple(n, e, d)
    }

    private fun generateRandomE(phi: BigInteger): BigInteger {
        val random = Random()
        var e: BigInteger

        do {

            e = BigInteger(phi.bitLength(), random)
        } while (e >= phi || e.gcd(phi) != BigInteger.ONE)

        return e
    }
}