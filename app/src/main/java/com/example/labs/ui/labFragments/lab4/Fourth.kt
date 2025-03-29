package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.RSAImpl
import com.example.labs.ui.MainLabsFragment
import com.example.labs.ui.GeneralViewModel

class Fourth : MainLabsFragment() {

    private var rsa = RSAImpl
    private val generalFunctions = GeneralFunctionsImpl
    private val viewModel: GeneralViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateHintForField(R.string.open_key, R.string.close_key)
        binding.btnForCheck.isVisible = true
        updateButtonConstraints(true)
        with(binding) {

            btnForCheck.isEnabled = false
            viewModel.isButtonEnabled.observe(viewLifecycleOwner) {
                    isEnabled -> binding.btnForCheck.isEnabled = true
            }
            btnForCheck.setOnClickListener {
                findNavController().navigate(R.id.action_fourth_to_checkForOpenKeyFragment)
            }
            //defould
            textForNameLab.setText(R.string.text_for_name_lab4)
            inputText.setHint(R.string.input_text)
            btnForBottomDialog.setText(R.string.create_keys)


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
                    generalFunctions.showShortToast(context, "Ключи не найдены", 500)
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
                val message = binding.inputText.text.toString().trim()
                val check = viewModel.countBitLength?.div(4) ?: 1
                checkKeyForCompliance(message, check)

                encrypt(message)
            } else {
                decrypt()
            }
        }
        //general error
        catch (e: Exception) {
            generalFunctions.showShortToast(requireContext(), "Ошибка: ${e.message}", 500)
        }
    }

    private fun decrypt() {
        val ciphertext = binding.inputText.text.toString()
        val privateKey = viewModel.privateKey
        val checkPrivateKey = binding.fieldForKey.text.toString()
        if (privateKey != null && checkPrivateKey == privateKey.second) {
            val message = rsa.decrypt(ciphertext, privateKey)
            binding.outputText.text = message
            binding.outputText.isVisible = true
            binding.buttonCopy.isVisible = true
        } else {
            generalFunctions.showShortToast(
                requireContext(),
                "Закрытый ключ не найден/не подходит",
                500
            )
        }
    }

    private fun encrypt(message: String) {
        val publicKey = viewModel.publicKey
        val checkPublicKey = binding.fieldForKey.text.toString()
        if (publicKey != null && checkPublicKey == publicKey.second) {
            val ciphertext = rsa.encrypt(message, publicKey)
            binding.outputText.text = ciphertext
            binding.outputText.isVisible = true
            binding.buttonCopy.isVisible = true
        } else {
            generalFunctions.showShortToast(
                requireContext(),
                "Открытый ключ не найден/не подходит",
                500
            )
        }
    }

    private fun checkKeyForCompliance(message: String, check: Int) {
        if (generalFunctions.containsCyrillic(message) && (message.length * 4.5).toInt() > check) {
            generalFunctions.showShortToast(
                requireContext(),
                "Нужно больше: ${(message.length * 4.5).toInt()} символа",
                500
            )
            return
        }
        if (message.length > check) {
            generalFunctions.showShortToast(
                requireContext(),
                "Нужен ключ побольше: ${message.length} символа",
                500
            )
            return
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
                binding.fieldForKey.setText(publicKey.second)
            }
        } else {
            val privateKey = viewModel.privateKey
            if (privateKey != null) {
                binding.fieldForKey.setText(privateKey.second)
            }
        }
    }
}











