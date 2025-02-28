package com.example.labs.data.lab3

import com.example.labs.domain.lab3.FeistelNetwork

object FeistelNetworkImpl: FeistelNetwork {

    override fun feistelNetwork(message: String, key: Long, rounds: Int, crypt: Boolean): Long {
        val block = message.hashCode()
        var left = (block ushr 32)
        var right = block

        if (crypt) {
            for (i in rounds - 1 downTo 0) {
                val temp = left
                left = right
                right = temp xor feistelFunction(right, key.toInt() + i)
            }
        } else {
            for (i in 0 until rounds) {
                val temp = left
                left = right
                right = temp xor feistelFunction(right, key.toInt() + i)
            }
        }

        return (left.toLong() shl 32) or (right.toLong() and 0xFFFFFFFF)
    }

    override fun feistelFunction(block: Int, key: Int): Int {
        return (block + key) xor (block ushr 16)
    }
}
