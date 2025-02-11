package com.example.labs.ui.labFragments
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.labs.R
import com.example.labs.data.lab1.CaesarCipherImpl
import com.example.labs.ui.MainLabsFragment


class First : MainLabsFragment() {

    private val cipher = CaesarCipherImpl

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonApply.setOnClickListener {
            applyCaesarCipher()
        }
        binding.symbolShift.setHint(R.string.number_of_shift)
        binding.textForNameLab.setText(R.string.сaesar_cipher)
    }


    private fun applyCaesarCipher() {
        val inputText = binding.inputText.text.toString()
        val shift = binding.symbolShift.text.toString().toInt()

        val result = if (selectedOption()) {
            cipher.encrypt(inputText, shift)
        } else {
            cipher.decrypt(inputText, shift)
        }
        binding.outputText.text = result
        binding.outputText.isVisible = true
        binding.buttonCopy.isVisible = true
    }
}

