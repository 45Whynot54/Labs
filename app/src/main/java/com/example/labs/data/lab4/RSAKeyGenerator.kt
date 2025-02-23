package com.example.labs.data.lab4

import java.math.BigInteger
import java.util.Random

object RSAKeyGenerator {

    private fun generatePrime(bitLength: Int): BigInteger {
        return BigInteger.probablePrime(bitLength, Random())
    }

    fun generateKeys(bitLength: Int): Triple<BigInteger, BigInteger, BigInteger> {
        var n: BigInteger
        var e: BigInteger
        var d: BigInteger
        do {
            val p = generatePrime(bitLength / 2)
            val q = generatePrime(bitLength / 2)
            n = p.multiply(q)
            val phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
            e = generateRandomE(phi)
            d = e.modInverse(phi)
        } while (n.bitLength() != bitLength)
        return Triple(n, e, d)
    }


    private fun generateRandomE(phi: BigInteger): BigInteger {
        val random = Random()
        var e: BigInteger

        do {
            e = BigInteger(phi.bitLength() - 1, random)
        } while (e >= phi || e.gcd(phi) != BigInteger.ONE)

        return e
    }

}
