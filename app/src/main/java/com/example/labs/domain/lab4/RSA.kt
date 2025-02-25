package com.example.labs.domain.lab4

import java.math.BigInteger

interface RSA {
    fun encrypt(message: String, publicKey: Pair<String, String>): String
    fun decrypt(ciphertext: String, privateKey: Pair<String, String>): String
}