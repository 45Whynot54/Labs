package com.example.labs.domain.lab4

import java.math.BigInteger

interface RSA {
    fun encrypt(message: String, publicKey: Pair<BigInteger, BigInteger>): BigInteger
    fun decrypt(ciphertext: BigInteger): String
}