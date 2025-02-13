package com.example.labs.domain

import android.content.Context

interface GeneralFunctions {
    fun copyText(context: Context, text: String)
    fun containsCyrillic(text: String): Boolean
    fun showShortToast(context: Context?, message: String, duration: Int)
}