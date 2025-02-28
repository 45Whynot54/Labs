package com.example.labs.data

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.example.labs.domain.GeneralFunctions
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object GeneralFunctionsImpl : GeneralFunctions {
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

    override fun textToLong(text: String): Long {
        val bytes = text.toByteArray(StandardCharsets.UTF_8)
        var result = 0L

        for (i in 0 until minOf(8, bytes.size)) {
            result = result shl 8 or (bytes[i].toLong() and 0xFF)
        }

        return result
    }

    override fun longToText(value: Long): String {
        val bytes = ByteArray(8)

        for (i in 7 downTo 0) {
            bytes[i] = (value shr (8 * (7 - i)) and 0xFF).toByte()
        }

        return String(bytes, StandardCharsets.UTF_8).trim { it <= ' ' }
    }
}
