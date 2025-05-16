package com.example.labs.data.lab5

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.random.Random

// Объект для работы с цифровой подписью
object DigitalSignature {

    // Параметры криптосистемы
    private val p = BigInteger("C7E5F152EFB3F1E96E6D5A56FEC1C3B74A1A41CDA77B2F65D52DA8BDBF50C1B1", 16)
    private val q = BigInteger("981E3B1F46C946F8C8A83394A6A7B6E7", 16)
    private val a = BigInteger("5", 16)


    // Закрытый и открытый ключи
    private var x: BigInteger = generatePrivateKey()
    private var y: BigInteger = modExp(a, x, p)

    /**
     * Функция модульного возведения в степень.
     * Вычисляет (base^exp) mod mod.
     *
     * @param base - основание
     * @param exp - показатель степени
     * @param mod - модуль
     * @return результат возведения в степень по модулю
     */
    private fun modExp(base: BigInteger, exp: BigInteger, mod: BigInteger): BigInteger {
        return base.modPow(exp, mod)
    }

    /**
     * Генерация закрытого ключа.
     * Создает случайное число в диапазоне [1, q-1].
     *
     * @return случайное число, удовлетворяющее условиям для закрытого ключа
     */
    private fun generatePrivateKey(): BigInteger {
        return BigInteger(q.bitLength(), java.security.SecureRandom()).mod(q.subtract(BigInteger.ONE)) + BigInteger.ONE
    }


    /**
     * Хеширование файла.
     * Создает хеш-значение содержимого файла.
     *
     * @param file - файл для хеширования
     * @return хеш-значение файла по модулю q
     */
    private fun hashFile(file: File): BigInteger {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().buffered().use { input ->
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return BigInteger(1, digest.digest()).mod(q)
    }


    /**
     * Подписание файла.
     * Генерирует цифровую подпись для указанного файла.
     *
     * @param file - файл для подписания
     * @return пара (r, s) - цифровая подпись
     */
    fun signFile(file: File): Pair<BigInteger, BigInteger> {
        // Получаем хеш файла
        var h = hashFile(file).mod(q)
        if (h == BigInteger.ZERO) h = BigInteger.ONE // Защита от нулевого хеша

        while (true) {
            // Генерация одноразового секретного числа k
            val k = generatePrivateKey()
            // Вычисление первой части подписи r = (a^k mod p) mod q
            val r = modExp(a, k, p).mod(q)
            if (r == BigInteger.ZERO) continue // Повторяем, если r = 0

            // Вычисление второй части подписи s = (x*r + k*h) mod q
            val s = (x.multiply(r).add(k.multiply(h))).mod(q)
            if (s == BigInteger.ZERO) continue // Повторяем, если s = 0

            return Pair(r, s) // Возвращаем подпись
        }
    }

    /**
     * Проверка цифровой подписи.
     *
     * @param file - файл, для которого проверяется подпись
     * @param r - первая часть подписи
     * @param s - вторая часть подписи
     * @return true, если подпись верна, false в противном случае
     */

    fun verifySignature(file: File, r: BigInteger, s: BigInteger): Boolean {
        // Проверка диапазонов r и s
        if (r <= BigInteger.ZERO || r >= q || s <= BigInteger.ZERO || s >= q) return false

        // Получаем хеш файла
        val h = hashFile(file).mod(q)
        if (h == BigInteger.ZERO) throw IllegalStateException("Hash cannot be zero") // Защита от нулевого хеша

        val w = s.modInverse(q)
        val z1 = (h * w).mod(q)
        val z2 = (r * w).mod(q)

        val u = (a.modPow(z1, p) * y.modPow(z2, p)).mod(p).mod(q)
        return u == r
    }
}