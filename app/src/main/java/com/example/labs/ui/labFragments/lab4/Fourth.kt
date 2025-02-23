package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.RSAImpl
import com.example.labs.databinding.FourthLabFragmentBinding
import com.example.labs.ui.ExplanationBottomSheetFragment

class Fourth : Fragment() {

    private var rsa = RSAImpl
    private val generalFunctions = GeneralFunctionsImpl
    private val viewModel: KeyViewModel by activityViewModels()

    private var _binding: FourthLabFragmentBinding? = null
    val binding: FourthLabFragmentBinding
        get() = _binding ?: throw RuntimeException("FourthLabFragmentBinding == null")

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            binding.outputText.text = ""
            binding.buttonCopy.isVisible = false
            binding.outputText.isVisible = false
            enableOrDisableButton()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FourthLabFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            binding.buttonCopy.setOnClickListener {
                generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
            }

            buttonCopy.setOnClickListener {
                generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
            }

            btnEncryptDecrypt.setOnClickListener {
                val publicKey = viewModel.publicKey
                val privateKey = viewModel.privateKey
                if (publicKey != null && privateKey != null) {
                    decryptOrEncrypt()
                } else {
                    generalFunctions.showShortToast(context, "Ключи не найдены", 200)
                }
            }
            switchType.setOnClickListener {
                checkTypeSwitch()
                binding.fieldForKey.text.clear()
            }
            createKeyBtn.setOnClickListener {
                showCreateBottomSheep()
            }
            backOnMain.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
            inputText.addTextChangedListener(textWatcher)
            fieldForKey.addTextChangedListener(textWatcher)

            explanationImageView.setOnClickListener {
                showExplanationLab4("Lab4")
            }
        }

    }

    private fun decryptOrEncrypt() {
        if (checkTypeSwitch()){
            encrypt()
        }
        else {
            decrypt()
        }
    }

    private fun encrypt() {
        val message = binding.inputText.text.toString()
        if (message.isNotEmpty()) {
            val publicKey = viewModel.publicKey
            if (publicKey != null) {
                val ciphertext = rsa.encrypt(message, publicKey)
                binding.outputText.text = ciphertext
                binding.outputText.isVisible = true
                binding.buttonCopy.isVisible = true
            } else {
                generalFunctions.showShortToast(requireContext(), "Открытый ключ не найден", 200)
            }
        } else {
            generalFunctions.showShortToast(requireContext(), "Введите сообщение", 200)
        }
    }

    private fun decrypt() {
        val ciphertext = binding.inputText.text.toString()
        if (ciphertext.isNotEmpty()) {
            val privateKey = viewModel.privateKey
            if (privateKey != null) {
                val message = rsa.decrypt(ciphertext, privateKey)
                binding.outputText.text = message
                binding.outputText.isVisible = true
                binding.buttonCopy.isVisible = true
            } else {
                generalFunctions.showShortToast(requireContext(), "Закрытый ключ не найден", 200)
            }
        } else {
            generalFunctions.showShortToast(requireContext(), "Введите зашифрованное сообщение", 200)
        }
    }

    private fun showCreateBottomSheep() {
        val bottomSheet = if (checkTypeSwitch()) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showExplanationLab4(labId: String) {
        val bottomSheet = ExplanationBottomSheetFragment.newInstance(labId)
        bottomSheet.show(parentFragmentManager, "ExplanationBottomSheetFragment")
    }

    private fun enableOrDisableButton() {
        val inputText = binding.inputText.text.toString()
        val KeyText = binding.fieldForKey.text.toString()
        binding.btnEncryptDecrypt.isEnabled = inputText.isNotEmpty() && KeyText.isNotEmpty()
    }

    private fun checkTypeSwitch(): Boolean {
        return when {
            binding.switchType.isChecked -> {
                binding.btnEncryptDecrypt.setText(R.string.decrypt)
//                binding.fieldForKey.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
//                binding.fieldForKey.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.fieldForKey.setHint(R.string.close_key)
                false
            }

            else -> {
                binding.btnEncryptDecrypt.setText(R.string.encrypt)
                binding.fieldForKey.inputType = InputType.TYPE_CLASS_TEXT
                binding.fieldForKey.transformationMethod = null
                binding.fieldForKey.setHint(R.string.open_key)
                true
            }
        }
    }
}











