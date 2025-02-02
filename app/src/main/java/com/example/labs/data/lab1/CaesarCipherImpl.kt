package com.example.labs.data.lab1

import com.example.labs.domain.lab1.Cipher

object CaesarCipherImpl : Cipher {

    override fun encrypt(text: String, shift: Int): String {
        return caesarCipher(text, shift)
    }

    override fun decrypt(text: String, shift: Int): String {
        return caesarCipher(text, -shift)
    }

    private fun caesarCipher(text: String, shift: Int): String {
        val builder = StringBuilder()
        for (char in text) {
            val base = if (char.isLowerCase()) 'a' else 'A'
            val position = char.code - base.code
            val shiftedPosition = (position + shift) % 26
            val newChar = (base.code + shiftedPosition).toChar()
            builder.append(newChar)
        }
        return builder.toString()
    }
}