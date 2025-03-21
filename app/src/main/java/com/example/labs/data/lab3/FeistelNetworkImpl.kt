package com.example.labs.data.lab3

import com.example.labs.domain.lab3.FeistelNetwork

object FeistelNetworkImpl : FeistelNetwork {

    private const val BLOCK_SIZE_BYTES = 8

    override fun feistelNetworkEncrypt(message: String, key: String, rounds: Int): String {
        val blocks = divideIntoBlocks(message)
        var encryptedMessage = ""
        blocks.forEach { block ->
            var encryptedBlock = block

            repeat(rounds) { round ->
                encryptedBlock = roundFunction(encryptedBlock, key, round)
            }
            encryptedMessage += encryptedBlock
        }
        return encryptedMessage
    }

    override fun feistelFunctionDecrypt(message: String, key: String, rounds: Int): String {
        val blocks = divideIntoBlocks(message)
        var decryptedMessage = ""
        blocks.forEach { block ->
            var decryptedBlock = block
            repeat(rounds) { round ->
                decryptedBlock = reverseRoundFunction(decryptedBlock, key, rounds - round - 1)
            }
            decryptedMessage += decryptedBlock
        }
        return decryptedMessage
    }

    private fun roundFunction(block: String, key: String, round: Int): String {
        val (left, right) = divideBlockIntoHalves(block)
        val roundKey = FeistelNetworkKeyGenerator.key(key, round)
        val newRight = xor(right, roundKey)
        return newRight + left
    }

    private fun reverseRoundFunction(block: String, key: String, round: Int): String {
        val (left, right) = divideBlockIntoHalves(block)
        val roundKey = FeistelNetworkKeyGenerator.key(key, round)
        val newLeft = xor(left, roundKey)
        return right + newLeft
    }

    private fun divideIntoBlocks(message: String): List<String> {
        val bytes = message.toByteArray()
        val blocks = mutableListOf<String>()
        val totalBlocks = (bytes.size + BLOCK_SIZE_BYTES - 1) / BLOCK_SIZE_BYTES

        for (i in 0 until totalBlocks) {
            val start = i * BLOCK_SIZE_BYTES
            val end = minOf(start + BLOCK_SIZE_BYTES, bytes.size)
            val blockBytes = bytes.copyOfRange(start, end)
            if (blockBytes.size < BLOCK_SIZE_BYTES) {
                val paddedBlock = blockBytes + ByteArray(BLOCK_SIZE_BYTES - blockBytes.size) { ' '.toByte() }
                blocks.add(paddedBlock.toString(Charsets.UTF_8))
            } else {
                blocks.add(blockBytes.toString(Charsets.UTF_8))
            }
        }
        return blocks
    }

    private fun divideBlockIntoHalves(block: String): Pair<String, String> {
        val mid = block.length / 2
        return Pair(block.substring(0, mid), block.substring(mid))
    }

    private fun xor(a: String, b: String): String {
        val repeatedKey = b.repeat((a.length / b.length) + 1).substring(0, a.length)
        return a.zip(repeatedKey).map { (aChar, bChar) -> aChar.code.xor(bChar.code).toChar() }.joinToString("")
    }
}