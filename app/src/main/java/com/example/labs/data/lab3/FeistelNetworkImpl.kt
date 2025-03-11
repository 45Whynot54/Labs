package com.example.labs.data.lab3

import com.example.labs.domain.lab3.FeistelNetwork

object FeistelNetworkImpl : FeistelNetwork {

    override fun feistelNetworkEncrypt(message: String, key: String, rounds: Int): String {
        var encryptedMessage = message
        repeat(rounds) {
            encryptedMessage = roundFunction(encryptedMessage, key)
        }
        return encryptedMessage
    }

    override fun feistelFunctionDecrypt(message: String, key: String, rounds: Int): String {
        var decryptedMessage = message
        repeat(rounds) {
            decryptedMessage = reverseRoundFunction(decryptedMessage, key)
        }
        return decryptedMessage
    }

    private fun roundFunction(message: String, key: String): String {
        val (left, right) = divideIntoBlocks(message)
        val newRight = xor(right, key)
        val newLeft = xor(left, newRight)
        return newLeft + newRight
    }

    private fun reverseRoundFunction(message: String, key: String): String {
        val (left, right) = divideIntoBlocks(message)
        val newLeft = xor(left, right)
        val newRight = xor(right, key)
        return newLeft + newRight
    }

    private fun divideIntoBlocks(message: String): Pair<String, String> {
        val mid = message.length / 2
        return Pair(message.substring(0, mid), message.substring(mid))
    }

    private fun xor(a: String, b: String): String {
        val repeatedKey = b.repeat((a.length / b.length) + 1).substring(0, a.length)
        return a.zip(repeatedKey).map { (aChar, bChar) -> aChar.code.xor(bChar.code).toChar() }.joinToString("")
    }
}
