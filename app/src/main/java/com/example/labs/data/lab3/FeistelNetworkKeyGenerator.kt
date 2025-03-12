package com.example.labs.data.lab3

import java.security.MessageDigest


object FeistelNetworkKeyGenerator {

    fun key(key: String, round: Int?): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val input = "$key$round".toByteArray()
        val hash = digest.digest(input)
        return hash.joinToString("") { "%02x".format(it) }
    }
}