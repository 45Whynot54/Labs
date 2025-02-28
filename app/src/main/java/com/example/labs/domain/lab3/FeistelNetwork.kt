package com.example.labs.domain.lab3

interface FeistelNetwork {
    fun feistelNetwork(message: String, key: Long, rounds: Int, crypt: Boolean): Long
    fun feistelFunction(block: Int, key: Int): Int
}