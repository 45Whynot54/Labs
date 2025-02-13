package com.example.labs.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.example.labs.domain.GeneralFunctions

object GeneralFunctionsImpl: GeneralFunctions {
    override fun copyText(context: Context, text: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
    }

    override fun containsCyrillic(text: String): Boolean {
        val cyrillicPattern = "[а-яА-Я]".toRegex()
        return cyrillicPattern.containsMatchIn(text)
    }
}