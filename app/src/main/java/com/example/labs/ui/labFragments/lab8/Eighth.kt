package com.example.labs.ui.labFragments.lab8

import android.view.View
import androidx.fragment.app.Fragment
import com.example.labs.databinding.EighthLabFragmentBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import java.math.BigInteger
import java.security.SecureRandom

class Eighth: Fragment() {

    private var _binding: EighthLabFragmentBinding? = null
    private val binding get() = _binding!!


    // Параметры эллиптической кривой (более безопасные)
    private val p = BigInteger("23") // простое поле
    private val a = BigInteger("1")  // коэффициент a
    private val b = BigInteger("1")  // коэффициент b
    private val G = Point(BigInteger("5"), BigInteger("1")) // базовая точка
    private val n = BigInteger("7")  // порядок базовой точки

    // Ключи участников
    private var user2PrivateKey: BigInteger? = null
    private var user2PublicKey: Point? = null
    private var user1PrivateKey: BigInteger? = null
    private var user1PublicKey: Point? = null
    private var sharedSecret: Point? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EighthLabFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGenerateKeys.setOnClickListener {
            try {
                generateKeys()
            } catch (e: Exception) {
                binding.tvResult.text = "Ошибка генерации: ${e.message}"
                e.printStackTrace()
            }
        }

        binding.btnExchange.setOnClickListener {
            try {
                performKeyExchange()
            } catch (e: Exception) {
                binding.tvResult.text = "Ошибка обмена: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    private fun generateKeys() {
        // Генерация ключей User 2
        user2PrivateKey = generatePrivateKey()
        user2PublicKey = multiplyPoint(G, user2PrivateKey!!)

        // Генерация ключей User 1
        user1PrivateKey = generatePrivateKey()
        user1PublicKey = multiplyPoint(G, user1PrivateKey!!)

        updateUI()
    }

    private fun generatePrivateKey(): BigInteger {
        return try {
            // Генерация безопасного случайного числа
            val secureRandom = SecureRandom()
            BigInteger(n.bitLength(), secureRandom)
                .mod(n.subtract(BigInteger.ONE))
                .add(BigInteger.ONE)
        } catch (e: Exception) {
            BigInteger.ONE // fallback, не использовать в production!
        }
    }

    private fun performKeyExchange() {
        if (user2PrivateKey == null || user1PrivateKey == null) {
            throw IllegalStateException("Ключи не сгенерированы")
        }

        // User 2 вычисляет общий секрет
        val user2Secret = multiplyPoint(user1PublicKey!!, user2PrivateKey!!)

        // User 1 вычисляет общий секрет
        val user1Secret = multiplyPoint(user2PublicKey!!, user1PrivateKey!!)

        // Проверка совпадения
        if (user2Secret != user1Secret) {
            throw IllegalStateException("Секреты не совпадают")
        }

        sharedSecret = user2Secret
        updateUI()
    }

    private fun updateUI() {
        binding.tvUser2Private.text = "Приватный User 2: ${user2PrivateKey ?: "нет"}"
        binding.tvUser2Public.text = "Публичный User 2: ${user2PublicKey ?: "нет"}"
        binding.tvUser1Private.text = "Приватный User 1: ${user1PrivateKey ?: "нет"}"
        binding.tvUser1Public.text = "Публичный User 1: ${user1PublicKey ?: "нет"}"

        sharedSecret?.let {
            binding.tvSharedSecretUser2.text = "Общий секрет: ${it.x}"
            binding.tvResult.text = "ECDH успешен!"
        } ?: run {
            binding.tvSharedSecretUser2.text = "Общий секрет: не вычислен"
        }
    }

    private fun multiplyPoint(point: Point, scalar: BigInteger): Point {
        require(scalar >= BigInteger.ZERO) { "Скаляр должен быть неотрицательным" }

        var result = Point(BigInteger.ZERO, BigInteger.ZERO) // нейтральный элемент
        var current = point
        var s = scalar

        while (s > BigInteger.ZERO) {
            if (s.testBit(0)) {
                result = addPoints(result, current)
            }
            current = addPoints(current, current)
            s = s.shiftRight(1)
        }

        return result
    }

    private fun addPoints(p1: Point, p2: Point): Point {
        // Точка на бесконечности
        if (p1.isInfinity()) return p2
        if (p2.isInfinity()) return p1

        // P + (-P) = O
        if (p1.x == p2.x && p1.y == p2.y.negate().mod(p)) {
            return Point(BigInteger.ZERO, BigInteger.ZERO)
        }

        // Удвоение точки
        if (p1 == p2) return doublePoint(p1)

        // Сложение различных точек
        val lambda = (p2.y - p1.y) * (p2.x - p1.x).modInverse(p) % p
        val x3 = (lambda.pow(2) - p1.x - p2.x) % p
        val y3 = (lambda * (p1.x - x3) - p1.y) % p

        return Point(x3, y3)
    }

    private fun doublePoint(point: Point): Point {
        require(!point.isInfinity()) { "Нельзя удваивать точку на бесконечности" }

        val lambda = (BigInteger("3") * point.x.pow(2) + a) *
                (BigInteger.TWO * point.y).modInverse(p) % p
        val x3 = (lambda.pow(2) - BigInteger.TWO * point.x) % p
        val y3 = (lambda * (point.x - x3) - point.y) % p

        return Point(x3, y3)
    }

    data class Point(val x: BigInteger, val y: BigInteger) {
        fun isInfinity() = x == BigInteger.ZERO && y == BigInteger.ZERO

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Point) return false
            return x == other.x && y == other.y
        }

        override fun hashCode() = 31 * x.hashCode() + y.hashCode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}