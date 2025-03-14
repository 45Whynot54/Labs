package com.example.labs.data.lab3

import com.example.labs.data.lab3.FeistelNetworkKeyGenerator.key
import com.example.labs.domain.lab3.FeistelNetwork

object FeistelNetworkImpl : FeistelNetwork {

    override fun feistelNetworkEncrypt(message: String, key: String, rounds: Int): String {
        var encryptedMessage = message
        repeat(rounds) { round ->
            encryptedMessage = roundFunction(encryptedMessage, key, round)
        }
        return encryptedMessage
    }

        override fun feistelFunctionDecrypt(message: String, key: String, rounds: Int): String {
        var decryptedMessage = message
        repeat(rounds) { round ->
            decryptedMessage = reverseRoundFunction(decryptedMessage, key, rounds - round - 1)
        }
        return decryptedMessage
    }

    private fun roundFunction(message: String, key: String, round: Int): String {
        val (left, right) = divideIntoBlocks(message)
        val roundKey = key(key, round)
        val newRight = xor(right, roundKey)
        val newLeft = xor(left, newRight)
        return newLeft + newRight
    }

    private fun reverseRoundFunction(message: String, key: String, round: Int): String {
        val (left, right) = divideIntoBlocks(message)
        val roundKey = key(key, round)
        val newLeft = xor(left, right)
        val newRight = xor(right, roundKey)
        return newLeft + newRight
    }

    private fun divideIntoBlocks(message: String): Pair<String, String> {
        val mid = message.length / 2
        val paddedMessage = if (message.length % 2 != 0) message + "" else message
        return Pair(paddedMessage.substring(0, mid), paddedMessage.substring(mid))
    }

    private fun xor(a: String, b: String): String {
        val repeatedKey = b.repeat((a.length / b.length) + 1).substring(0, a.length)
        return a.zip(repeatedKey).map { (aChar, bChar) -> aChar.code.xor(bChar.code).toChar() }.joinToString("")
    }
}