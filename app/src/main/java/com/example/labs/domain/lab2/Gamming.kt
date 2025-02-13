package com.example.labs.domain.lab2


interface Gamming {
    fun generateKey(length: Int): ByteArray
    fun encrypt(text: String, key: ByteArray): ByteArray
    fun decrypt(encryptedText: ByteArray, key: ByteArray): String
}