package com.example.labs.data.lab6

import android.graphics.Bitmap
import android.graphics.Color

object Steganography {

    fun embedMessage(image: Bitmap, message: String): Bitmap {
        val width = image.width
        val height = image.height
        val mutableBitmap = image.copy(Bitmap.Config.ARGB_8888, true)
        var messageIndex = 0
        var messageBitIndex = 0
        var currentByte = if (message.isNotEmpty()) message[messageIndex].code else 0

        loop@ for (y in 0 until height) {
            for (x in 0 until width) {
                // Получаем цвет текущего пикселя
                val pixel = mutableBitmap.getPixel(x, y)

                // Разделяем цвет на компоненты
                var red = Color.red(pixel)
                var green = Color.green(pixel)
                var blue = Color.blue(pixel)

                for (channel in 0..2) {
                    // Если обработали 8 бит, переходим к следующему символу
                    if (messageBitIndex >= 8) {
                        messageIndex++
                        messageBitIndex = 0
                        currentByte = if (messageIndex < message.length) message[messageIndex].code else 0
                    }

                    // Проверка завершения сообщения (добавляем нулевой байт в конце)
                    if (messageIndex >= message.length && currentByte == 0 && messageBitIndex == 0) {
                        break@loop
                    }

                    // Получаем бит для встраивания (0 или 1)
                    val bit = if (messageIndex < message.length || currentByte != 0) {
                        (currentByte shr messageBitIndex) and 1 // Сдвиг и маска для получения нужного бита
                    } else {
                        0 // Заполняем нулями после окончания сообщения
                    }

                    // Модифицируем младший бит выбранного канала
                    when (channel) {
                        0 -> red = (red and 0xFE) or bit   //0xFE = 11111110 (обнуляем младший бит)
                        1 -> green = (green and 0xFE) or bit
                        2 -> blue = (blue and 0xFE) or bit
                    }

                    messageBitIndex++
                }

                // Обновляем пиксель новыми значениями
                mutableBitmap.setPixel(x, y, Color.rgb(red, green, blue))

                if (messageIndex >= message.length && currentByte == 0 && messageBitIndex >= 8) {
                    break@loop
                }
            }
        }

        return mutableBitmap
    }

    fun extractMessage(image: Bitmap): String {
        val width = image.width
        val height = image.height
        val message = StringBuilder()

        var currentByte = 0
        var bitIndex = 0

        for (y in 0 until height) {
            for (x in 0 until width) {
                val pixel = image.getPixel(x, y)


                val bits = listOf(
                    Color.red(pixel) and 1,   // Младший бит красного
                    Color.green(pixel) and 1, // Младший бит зеленого
                    Color.blue(pixel) and 1   // Младший бит синего
                )

                // Обрабатываем все 3 бита (R, G, B) из текущего пикселя
                for (bit in bits) {
                    // Добавляем бит в текущий байт
                    currentByte = currentByte or (bit shl bitIndex)
                    bitIndex++

                    if (bitIndex >= 8) {
                        // Проверка терминатора сообщения (нулевой байт)
                        if (currentByte == 0) {
                            return message.toString()
                        }
                        // Добавляем символ в результат и сбрасываем счетчики
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