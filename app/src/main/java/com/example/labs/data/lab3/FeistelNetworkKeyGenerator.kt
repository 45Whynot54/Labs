package com.example.labs.data.lab3

import java.security.SecureRandom


object FeistelNetworkKeyGenerator {

    fun keyGenerator(): Long {
        val secureRandom = SecureRandom()
        return secureRandom.nextLong()
    }
}