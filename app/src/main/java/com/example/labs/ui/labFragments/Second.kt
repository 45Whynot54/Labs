package com.example.labs.ui.labFragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.labs.R
import com.example.labs.data.lab2.GammingImpl
import com.example.labs.ui.MainLabsFragment

class Second: MainLabsFragment() {

    private val gamming = GammingImpl

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonApply.setOnClickListener {
            applyGamming()
        }
        binding.symbolShift.setHint(R.string.key_gamming)
        binding.textForNameLab.setText(R.string.name_for_gamming)
        binding.btnCodeReview.setOnClickListener {
            showExplanation("Lab2")
        }
        binding.explanationImageView.setOnClickListener {
            showExplanation("Lab2Exp")
        }
    }

    private fun applyGamming() {
        val inputText = binding.inputText.text.toString()
        val keyText = binding.symbolShift.text.toString()

        val key = if (keyText.length < inputText.length) {
            keyText.toByteArray().copyOf(inputText.length)
        } else {
            keyText.toByteArray()
        }

        try {
            val result = if (selectedOption()) {
                val encryptedBytes = gamming.encrypt(inputText, key)
                encryptedBytes.joinToString("") { byte -> "%02x".format(byte) }
            } else {
                val encryptedBytes = inputText.chunked(2)
                    .map { it.toInt(16).toByte() }
                    .toByteArray()

                gamming.decrypt(encryptedBytes, key)
            }

            binding.outputText.text = result
            binding.outputText.isVisible = true
            binding.buttonCopy.isVisible = true
        } catch (e: Exception) {
            binding.outputText.text = "Ошибка: Поддержка только английских букв"
            binding.outputText.isVisible = true
            binding.buttonApply.isEnabled = false
        }
    }
}