package com.example.labs.data.lab6

import android.graphics.Bitmap
import android.graphics.Color

object Steganography {

    // Встраивание сообщения в изображение
    fun embedMessage(image: Bitmap, message: String): Bitmap {
        val width = image.width
        val height = image.height
        val mutableBitmap = image.copy(Bitmap.Config.ARGB_8888, true)

        var messageIndex = 0
        var messageBitIndex = 0
        var currentByte = if (message.isNotEmpty()) message[messageIndex].code else 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                if (messageIndex >= message.length && messageBitIndex == 0) {
                    return mutableBitmap
                }

                val pixel = mutableBitmap.getPixel(x, y)
                val red = Color.red(pixel)
                val green = Color.green(pixel)
                val blue = Color.blue(pixel)

                // Встраиваем по одному биту в каждый канал (R, G, B)
                val newRed = embedBit(red, currentByte, messageBitIndex)
                val newGreen = embedBit(green, currentByte, messageBitIndex + 1)
                val newBlue = embedBit(blue, currentByte, messageBitIndex + 2)

                val newPixel = Color.rgb(newRed, newGreen, newBlue)
                mutableBitmap.setPixel(x, y, newPixel)

                messageBitIndex += 3
                if (messageBitIndex >= 8) {
                    messageBitIndex = 0
                    messageIndex++
                    if (messageIndex < message.length) {
                        currentByte = message[messageIndex].code
                    } else {
                        currentByte = 0 // Добавляем завершающий байт
                    }
                }
            }
        }

        return mutableBitmap
    }

    // Встраивание одного бита в цветовой канал
    private fun embedBit(color: Int, byte: Int, bitIndex: Int): Int {
        val bit = (byte shr bitIndex) and 1 // Извлекаем бит
        return (color and 0xFE) or bit // Заменяем младший бит
    }

    // Извлечение сообщения из изображения
    fun extractMessage(image: Bitmap): String {
        val width = image.width
        val height = image.height
        val message = StringBuilder()

        var currentByte = 0
        var bitIndex = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = image.getPixel(x, y)

                // Извлекаем биты строго по одному из каждого канала
                val bits = listOf(
                    Color.red(pixel) and 1,
                    Color.green(pixel) and 1,
                    Color.blue(pixel) and 1
                )

                for (bit in bits) {
                    currentByte = currentByte or (bit shl bitIndex)
                    bitIndex++
                    if (bitIndex >= 8) {
                        if (currentByte == 0) {
                            return message.toString() // Ноль — признак конца сообщения
                        }
                        message.append(currentByte.toChar())
                        currentByte = 0
                        bitIndex = 0
                    }
                }
            }
        }

        return message.toString()
    }
}