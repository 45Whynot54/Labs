package com.example.labs.data.lab8

object Config {
    fun getDefaultParams(): ECParameters = ECParameters(
        a = -1,
        b = 1,
        p = 29,
        basePoint = Point(9, 27),
        privateKeyA = 4,
        privateKeyB = 17
    )
}