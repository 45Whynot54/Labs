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

            radioGroup.setOnCheckedChangeListener { _, _ ->
                inputText.text.clear()
                fieldForKey.text.clear()
                updateHintForField(R.string.open_key, R.string.close_key)
            }
            textForNameLab.setText(R.string.text_for_name_lab4)
            inputText.setHint(R.string.input_text)
            buttonCopy.setOnClickListener {
                generalFunctions.copyText(requireContext(), outputText.text.toString())
            }
            btnForBottomDialog.setOnClickListener {
                showExplanation("Lab4")
            }
            explanationImageView.setOnClickListener {
                showExplanation("Lab4Exp")
            }
            btnEncryptOrDecrypt.setOnClickListener {

                val publicKey = viewModel.publicKey
                val privateKey = viewModel.privateKey
                if (publicKey != null && privateKey != null) {
                    encryptOrDecrypt()
                } else {
                    generalFunctions.showShortToast(context, "Ключи не найдены", 200)
                }
            }
            btnForBottomDialog.setOnClickListener {
                showCreateBottomSheep()
            }
            backOnMain.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            explanationImageView.setOnClickListener {
                showExplanation("Lab4")
            }
        }

    }

    private fun encryptOrDecrypt() {
        try {
            if (selectedOption()) {
                val message = binding.inputText.text.toString()
                if (message.isNotEmpty()) {
                    val publicKey = viewModel.publicKey
                    if (publicKey != null) {
                        val checkPublicKey = binding.fieldForKey.text.toString()
                        if (checkPublicKey == publicKey.second.toString()) { // Сравниваем e
                            val ciphertext = rsa.encrypt(message, publicKey)
                            binding.outputText.text = ciphertext
                            binding.outputText.isVisible = true
                            binding.buttonCopy.isVisible = true
                        } else {
                            generalFunctions.showShortToast(
                                requireContext(),
                                "Некорректный открытый ключ",
                                200
                            )
                        }
                    } else {
                        generalFunctions.showShortToast(
                            requireContext(),
                            "Открытый ключ не найден",
                            200
                        )
                    }
                } else {
                    generalFunctions.showShortToast(
                        requireContext(),
                        "Введите сообщение для шифрования",
                        200
                    )
                }
            } else {
                val ciphertext = binding.inputText.text.toString()
                if (ciphertext.isNotEmpty()) {
                    val privateKey = viewModel.privateKey
                    if (privateKey != null) {
                        val checkPrivateKey = binding.fieldForKey.text.toString()
                        if (checkPrivateKey == privateKey.second.toString()) {
                            val message = rsa.decrypt(ciphertext, privateKey)
                            binding.outputText.text = message
                            binding.outputText.isVisible = true
                            binding.buttonCopy.isVisible = true
                        } else {
                            generalFunctions.showShortToast(
                                requireContext(),
                                "Некорректный закрытый ключ",
                                200
                            )
                        }
                    } else {
                        generalFunctions.showShortToast(
                            requireContext(),
                            "Закрытый ключ не найден",
                            200
                        )
                    }
                } else {
                    generalFunctions.showShortToast(
                        requireContext(),
                        "Введите зашифрованное сообщение",
                        200
                    )
                }
            }
        } catch (e: Exception) {
            generalFunctions.showShortToast(
                requireContext(),
                "Ошибка: ${e.message}",
                200
            )
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
}











