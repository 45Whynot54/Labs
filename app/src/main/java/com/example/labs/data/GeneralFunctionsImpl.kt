package com.example.labs.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
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

    override fun showShortToast(context: Context?, message: String, duration: Int) {
            if (context == null) return

            val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast.show()

            Handler(Looper.getMainLooper()).postDelayed({
                toast.cancel()
            }, duration.toLong())
    }
}
