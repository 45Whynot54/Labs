package com.example.labs.data.lab2

import com.example.labs.domain.lab2.Gamming
import kotlin.experimental.xor

object GammingImpl: Gamming {

    override fun encrypt(text: String, key: ByteArray): ByteArray {
        val textBytes = text.toByteArray()
        return textBytes.mapIndexed { index, byte ->
            byte xor key[index]
        }.toByteArray()
    }

    override fun decrypt(encryptedText: ByteArray, key: ByteArray): String {
        val decryptedBytes = encryptedText.mapIndexed { index, byte ->
            byte xor key[index]
        }.toByteArray()
        return String(decryptedBytes)
    }
}