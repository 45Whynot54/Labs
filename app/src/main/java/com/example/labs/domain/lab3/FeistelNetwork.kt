package com.example.labs.domain.lab3

interface FeistelNetwork {
    fun feistelNetworkEncrypt(message: String, key: String, rounds: Int): String
    fun feistelFunctionDecrypt(message: String, key: String, rounds: Int): String
}