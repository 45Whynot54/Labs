package com.example.labs.ui
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.labs.R
import com.example.labs.data.lab1.CaesarCipherImpl


class FirstLabFragment : MainLabsFragment() {

    private val cipher = CaesarCipherImpl

    init {
        binding.textForNameLab.resources.getString(R.string.app_name)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonApply.setOnClickListener {
            applyCaesarCipher()
        }

    }
    private fun applyCaesarCipher() {
        val inputText = binding.inputText.text.toString()
        val selectedOption = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.btn_encrypt -> true
            R.id.btn_decrypt -> false
            else -> throw IllegalArgumentException("Неправильный выбор!")
        }
        val shift = binding.symbolShift.text.toString().toInt()
        val result = if (selectedOption) {
            cipher.encrypt(inputText, shift)
        } else {
            cipher.decrypt(inputText, shift)
        }
        binding.outputText.text = result
        binding.outputText.isVisible = true
        binding.buttonCopy.isVisible = true
    }

}

