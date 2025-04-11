package com.example.labs.ui.labFragments.lab8

import android.view.View
import androidx.fragment.app.Fragment
import com.example.labs.databinding.EighthLabFragmentBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.labs.data.lab8.ECDHKeyExchange
import com.example.labs.data.lab8.EllipticCurve
import java.math.BigInteger

class Eighth : Fragment() {
    private var _binding: EighthLabFragmentBinding? = null
    private val binding get() = _binding!!

    private val curve = EllipticCurve(
        p = BigInteger("23"),
        a = BigInteger("1"),
        b = BigInteger("1"),
        G = EllipticCurve.Point(BigInteger("5"), BigInteger("1")),
        n = BigInteger("7")
    )

    private val user1 = ECDHKeyExchange(curve)
    private val user2 = ECDHKeyExchange(curve)
    private var sharedSecret: EllipticCurve.Point? = null

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
        user1.generateKeys()
        user2.generateKeys()
        updateUI()
    }

    private fun performKeyExchange() {
        val user1Secret = user1.computeSharedSecret(user2.publicKey!!)
        val user2Secret = user2.computeSharedSecret(user1.publicKey!!)

        if (user1Secret != user2Secret) {
            throw IllegalStateException("Секреты не совпадают")
        }

        sharedSecret = user1Secret
        updateUI()
    }

    private fun updateUI() {
        binding.tvUser2Private.text = "Приватный User 2: ${user2.privateKey ?: "нет"}"
        binding.tvUser2Public.text = "Публичный User 2: ${user2.publicKey ?: "нет"}"
        binding.tvUser1Private.text = "Приватный User 1: ${user1.privateKey ?: "нет"}"
        binding.tvUser1Public.text = "Публичный User 1: ${user1.publicKey ?: "нет"}"

        sharedSecret?.let {
            binding.secret.text = "Общий секрет: ${it.x}"
            binding.tvResult.text = "ECDH успешен!"
        } ?: run {
            binding.secret.text = "..."
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}