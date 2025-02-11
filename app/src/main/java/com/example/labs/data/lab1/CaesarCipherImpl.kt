package com.example.labs.data.lab1

import com.example.labs.domain.lab1.CipherCaesar

object CaesarCipherImpl : CipherCaesar {


    override fun encrypt(text: String, shift: Int): String {
        return caesarCipher(text, shift)
    }

    override fun decrypt(text: String, shift: Int): String {
        return caesarCipher(text, -shift)
    }

    override fun caesarCipher(text: String, shift: Int): String {
        val builder = StringBuilder()
        for (char in text) {
            if (char.isLetter()) {
                val base = when (char) {
                    in 'a'..'z' -> 'a'
                    in 'A'..'Z' -> 'A'
                    in 'а'..'я' -> 'а'
                    in 'А'..'Я' -> 'А'
                    else -> continue
                }
                val alphabetSize = when (base) {
                    'a', 'A' -> 26
                    'а', 'А' -> 32
                    else -> throw IllegalArgumentException("Unsupported alphabet")
                }
                val position = char.code - base.code
                val shiftedPosition = (position + shift) % alphabetSize
                val newChar = (base.code + shiftedPosition).toChar()
                builder.append(newChar)
            } else if (char.isDigit()) {
                val newChar = ((char - '0' + shift) % 10 + '0'.code).toChar()
                builder.append(newChar)
            } else {
                builder.append(char)
            }
        }
        return builder.toString()
    }
}