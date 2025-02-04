package com.example.labs.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.example.labs.domain.AllFunction

object AllFunctionImpl: AllFunction {
    override fun copyText(context: Context, text: String) {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
    }
}