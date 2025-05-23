package com.example.labs.ui.labFragments.lab1
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import com.example.labs.R
import com.example.labs.data.lab1.CaesarCipherImpl
import com.example.labs.ui.MainLabsFragment


class First : MainLabsFragment() {

    private val cipher = CaesarCipherImpl

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notWork.isVisible = false
        binding.fieldForKey.setInputType(InputType.TYPE_CLASS_NUMBER)

        binding.btnEncryptOrDecrypt.setOnClickListener {
            applyCaesarCipher()
        }
        binding.fieldForKey.setHint(R.string.number_of_shift)
        binding.textForNameLab.setText(R.string.сaesar_cipher)
        binding.btnForBottomDialog.isVisible = false
    }


    private fun applyCaesarCipher() {
        val inputText = binding.inputText.text.toString()
        val shift = binding.fieldForKey.text.toString().toInt()

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

