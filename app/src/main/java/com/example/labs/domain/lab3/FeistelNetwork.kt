package com.example.labs.domain.lab3

interface FeistelNetwork {
    fun feistelNetwork(block: Int, key: Int, rounds: Int): Int
    fun feistelFunction(block: Int, key: Int): Int
}