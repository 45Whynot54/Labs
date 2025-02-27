package com.example.labs.data.lab3

import com.example.labs.domain.lab3.FeistelNetwork
import kotlin.experimental.xor

object FeistelNetworkImpl: FeistelNetwork {

    override fun feistelNetwork(block: Int, key: Int, rounds: Int): Int {
        var left = (block ushr 32)
        var right = block

        for (i in 0 until rounds) {
            val temp = left
            left = right
            right = temp xor feistelFunction(right, key)
        }


        return (left shl 32) or (right and 0xFFFF)
    }


    override fun feistelFunction(block: Int, key: Int): Int {
        return (block xor key)
    }
}