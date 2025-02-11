package com.example.labs.domain.lab1

interface CipherCaesar {
    fun encrypt(text: String, shift: Int): String
    fun decrypt(text: String, shift: Int): String
    fun caesarCipher(text: String, shift: Int): String
}