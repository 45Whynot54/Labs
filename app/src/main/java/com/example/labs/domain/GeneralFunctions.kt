package com.example.labs.domain

import android.content.Context
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

interface GeneralFunctions {
    fun copyText(context: Context, text: String)
    fun containsCyrillic(text: String): Boolean
    fun showShortToast(context: Context?, message: String, duration: Int)
    fun textToLong(text: String): Long
    fun longToText(value: Long): String
}