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
import androidx.fragment.app.viewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.KeyStorage
import com.example.labs.data.lab4.RSAImpl
import com.example.labs.databinding.FourthLabFragmentBinding
import com.example.labs.ui.ExplanationBottomSheetFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigInteger

class Fourth : Fragment() {


    private lateinit var keyStorage: KeyStorage
    private var rsa = RSAImpl
    private val generalFunctions = GeneralFunctionsImpl

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

        keyStorage = KeyStorage(requireContext())




        with(binding) {

            binding.buttonCopy.setOnClickListener {
                generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
            }

            buttonCopy.setOnClickListener {
                generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
            }

            btnEncryptDecrypt.setOnClickListener {
                decryptOrEncrypt()
                btnEncryptDecrypt.isEnabled = false
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



    private fun decryptOrEncrypt(){
        if (checkTypeSwitch()){
            decrypt()
        }
        else {
            encrypt()
        }
    }

    private fun encrypt() {
        CoroutineScope(Dispatchers.Main).launch {
            val message = binding.inputText.text.toString()
            if (message.isNotEmpty()) {
                // Получаем открытый ключ из текстового поля
                val publicKeyText = binding.fieldForKey.text.toString()
                val (n, e) = parseKey(publicKeyText) // Парсим ключ

                if (n != null && e != null) {
                    // Шифруем сообщение
                    val ciphertext = rsa.encrypt(message, Pair(n, e))
                    binding.outputText.setText(ciphertext)
                    binding.outputText.visibility = View.VISIBLE
                    binding.buttonCopy.isVisible = true
                    binding.btnEncryptDecrypt.visibility = View.VISIBLE
                } else {
                    generalFunctions.showShortToast(context, "Некорректный открытый ключ", 200)
                }
            } else {
                generalFunctions.showShortToast(context, "Введите сообщение", 200)
            }
        }
    }


    private fun decrypt() {
        CoroutineScope(Dispatchers.Main).launch {
            val ciphertext = binding.outputText.text.toString().replace("Зашифрованное сообщение: ", "")
            if (ciphertext.isNotEmpty()) {
                // Получаем закрытый ключ из текстового поля
                val privateKeyText = binding.fieldForKey.text.toString()
                val (n, d) = parseKey(privateKeyText) // Парсим ключ

                if (n != null && d != null) {
                    // Расшифровываем сообщение
                    val message = rsa.decrypt(ciphertext, Pair(n, d))
                    binding.outputText.setText(message)
                    binding.outputText.visibility = View.VISIBLE
                    binding.buttonCopy.isVisible = true
                    binding.btnEncryptDecrypt.visibility = View.VISIBLE
                } else {
                    generalFunctions.showShortToast(context, "Некорректный закрытый ключ", 200)
                }
            } else {
                generalFunctions.showShortToast(context, "Введите зашифрованное сообщение", 200)
            }
        }
    }

    private fun parseKey(keyText: String): Pair<BigInteger?, BigInteger?> {
        val parts = keyText.split(" ") // Разделяем ключ по пробелу
        return if (parts.size == 2) {
            val n = parts[0].toBigIntegerOrNull()
            val eOrD = parts[1].toBigIntegerOrNull()
            Pair(n, eOrD)
        } else {
            Pair(null, null)
        }
    }

    private fun showCreateBottomSheep() {
        val bottomSheet = if (checkTypeSwitch()) {
            CreationKeyBottomSheetFragment().apply {
                onKeysGenerated = { openKey, _ ->
                    binding.fieldForKey.setText(openKey)
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
                binding.fieldForKey.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.fieldForKey.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.fieldForKey.setHint(R.string.close_key)
                true
            }

            else -> {
                binding.btnEncryptDecrypt.setText(R.string.encrypt)
                binding.fieldForKey.inputType = InputType.TYPE_CLASS_TEXT
                binding.fieldForKey.transformationMethod = null
                binding.fieldForKey.setHint(R.string.open_key)
                false
            }
        }
    }
}











