package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.RSAImpl
import com.example.labs.ui.MainLabsFragment

class Fourth : MainLabsFragment() {

    private var rsa = RSAImpl
    private val generalFunctions = GeneralFunctionsImpl
    private val viewModel: KeyViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateHintForField(R.string.open_key, R.string.close_key)

        with(binding) {

            //defould
            textForNameLab.setText(R.string.text_for_name_lab4)
            inputText.setHint(R.string.input_text)
            btnForBottomDialog.setText(R.string.create_key)
            explanationImageView.setOnClickListener {
                showExplanation("Lab4")
            }
            btnForBottomDialog.setOnClickListener {
                showExplanation("Lab4")
            }
            explanationImageView.setOnClickListener {
                showExplanation("Lab4Exp")
            }

            //specific
            radioGroup.setOnCheckedChangeListener { _, _ ->
                updateTextForButton()
                inputText.text.clear()
                fieldForKey.text.clear()
                updateKeysInViewModel()
                updateHintForField(R.string.open_key, R.string.close_key)
            }

            btnEncryptOrDecrypt.setOnClickListener {
                val publicKey = viewModel.publicKey
                val privateKey = viewModel.privateKey
                if (publicKey != null && privateKey != null) {
                    encryptOrDecrypt()
                } else {
                    generalFunctions.showShortToast(context, "Ключи не найдены", 400)
                }
                btnEncryptOrDecrypt.isEnabled = false
            }
            btnForBottomDialog.setOnClickListener {
                showCreateBottomSheep()
            }
        }
    }

    private fun encryptOrDecrypt() {
        try {
            if (selectedOption()) {
                //encrypt

                val message = binding.inputText.text.toString()
                //check length and Russian alphabet
                if (message.length > 10) return
                if (generalFunctions.containsCyrillic(message)) {
                    generalFunctions.showShortToast(requireContext(),"Русский язык не работает", 500)
                    binding.inputText.text.clear()
                    return
                }

                val publicKey = viewModel.publicKey
                val checkPublicKey = binding.fieldForKey.text.toString()
                if (publicKey != null && checkPublicKey == publicKey.second) {
                    val ciphertext = rsa.encrypt(message, publicKey)
                    binding.outputText.text = ciphertext
                    binding.outputText.isVisible = true
                    binding.buttonCopy.isVisible = true
                } else { generalFunctions.showShortToast(requireContext(), "Открытый ключ не найден/не подходит", 500) }

                //decrypt
            } else {
                val ciphertext = binding.inputText.text.toString()
                val privateKey = viewModel.privateKey
                val checkPrivateKey = binding.fieldForKey.text.toString()
                if (privateKey != null && checkPrivateKey == privateKey.second.toString()) {
                    val message = rsa.decrypt(ciphertext, privateKey)
                    binding.outputText.text = message
                    binding.outputText.isVisible = true
                    binding.buttonCopy.isVisible = true
                } else { generalFunctions.showShortToast(requireContext(), "Закрытый ключ не найден/не подходит", 500) }
            }
        }
        //general error
        catch (e: Exception) {
            generalFunctions.showShortToast(requireContext(), "Ошибка: ${e.message}", 500)
        }
    }

    private fun showCreateBottomSheep() {
        val bottomSheet = if (selectedOption()) {
            CreationKeyBottomSheetFragment().apply {
                onKeysGenerated = { publicKey, _ ->
                    binding.fieldForKey.setText(publicKey)
                }
            }
        } else {
            CreationKeyBottomSheetFragment().apply {
                onKeysGenerated = { _, closeKey ->
                    binding.fieldForKey.setText(closeKey)
                }
            }
        }
        bottomSheet.show(childFragmentManager, "KeyGenerationBottomSheet")
    }

    private fun updateKeysInViewModel() {
        if (selectedOption()) {
            val publicKey = viewModel.publicKey
            if (publicKey != null) {
                binding.fieldForKey.setText(publicKey.second.toString())
            }
        } else {
            val privateKey = viewModel.privateKey
            if (privateKey != null) {
                binding.fieldForKey.setText(privateKey.second.toString())
            }
        }
    }
}











