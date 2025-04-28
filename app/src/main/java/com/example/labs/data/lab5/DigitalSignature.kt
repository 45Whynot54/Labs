package com.example.labs.data.lab5

import java.io.File
import java.math.BigInteger
import kotlin.random.Random

object DigitalSignature {

    // Параметры криптосистемы
    private val p = BigInteger("C7E5F152EFB3F1E96E6D5A56FEC1C3B74A1A41CDA77B2F65D52DA8BDBF50C1B1", 16)
    private val q = BigInteger("981E3B1F46C946F8C8A83394A6A7B6E7", 16)
    private val a = BigInteger("5", 16)

    // Закрытый и открытый ключи
    private var x: BigInteger = generatePrivateKey()
    private var y: BigInteger = modExp(a, x, p)

    private fun modExp(base: BigInteger, exp: BigInteger, mod: BigInteger): BigInteger {
        return base.modPow(exp, mod)
    }

    private fun generatePrivateKey(): BigInteger {
        val secureRandom = java.security.SecureRandom()
        return BigInteger(q.bitLength(), secureRandom).let { randomNum ->
            randomNum.mod(q - BigInteger.ONE) + BigInteger.ONE
        }
    }

    private fun hashFile(file: File): BigInteger {
        var hashValue = BigInteger.ZERO
        file.inputStream().buffered().use { input ->
            val buffer = ByteArray(2)
            while (true) {
                val bytesRead = input.read(buffer)
                if (bytesRead == -1) break

                val blockVal = if (bytesRead == 2) {
                    BigInteger(buffer)
                } else {
                    BigInteger(buffer.copyOf(bytesRead) + byteArrayOf(0x00))
                }

                hashValue = hashValue.xor(blockVal)
                hashValue = hashValue.shiftRight(1)
            }
        }
        return hashValue.mod(q)
    }

    fun signFile(file: File): Pair<BigInteger, BigInteger> {
        var h = hashFile(file).mod(q)
        if (h == BigInteger.ZERO) h = BigInteger.ONE

        while (true) {
            val k = generatePrivateKey()
            val r = modExp(a, k, p).mod(q)
            if (r == BigInteger.ZERO) continue

            val s = (x.multiply(r).add(k.multiply(h))).mod(q)
            if (s == BigInteger.ZERO) continue

            return Pair(r, s)
        }
    }

    fun verifySignature(file: File, r: BigInteger, s: BigInteger): Boolean {
        if (r <= BigInteger.ZERO || r >= q || s <= BigInteger.ZERO || s >= q) {
            return false
        }

        var h = hashFile(file).mod(q)
        if (h == BigInteger.ZERO) h = BigInteger.ONE

        val v = h.modPow(q.subtract(BigInteger.TWO), q)
        val z1 = s.multiply(v).mod(q)
        val z2 = q.subtract(r).multiply(v).mod(q)
        val u = modExp(a, z1, p).multiply(modExp(y, z2, p)).mod(p).mod(q)

        return u == r
    }
}