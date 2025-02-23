package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        val publicKey = keyStorage.getPublicKey()
        val privateKey = keyStorage.getPrivateKey()

        if (publicKey == null || privateKey == null) {
            generalFunctions.showShortToast(context, "Ключи не найдены", 200)
            return
        }
        RSAImpl.initialize(publicKey.first, publicKey.second, privateKey.second)


        with(binding) {

            buttonCopy.setOnClickListener {
                generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
            }

            btnEncryptDecrypt.setOnClickListener {
                decryptOrEncrypt(publicKey)
            }
            switchType.setOnClickListener {
                checkTypeSwitch()
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



    private fun decryptOrEncrypt(publicKey: Pair<BigInteger, BigInteger>){
        if (checkTypeSwitch()){
            decrypt()
        }
        else {
            if (publicKey != null) {
                encrypt(publicKey)
            } else {
                generalFunctions.showShortToast(context, "Публичный ключ не найден", 200)
            }
        }
    }

    private fun encrypt(publicKey: Pair<BigInteger, BigInteger>) {
        CoroutineScope(Dispatchers.Main).launch {
            val message = binding.inputText.text.toString()
            if (message.isNotEmpty()) {
                val ciphertext = publicKey.let { rsa.encrypt(message, it) }
                binding.outputText.setText(ciphertext)
                binding.outputText.visibility = View.VISIBLE
                binding.buttonCopy.isVisible = true
                binding.btnEncryptDecrypt.visibility = View.VISIBLE
            } else {
                generalFunctions.showShortToast(context, "Ключи не найдены", 200)
            }
        }
    }


    private fun decrypt() {
        CoroutineScope(Dispatchers.Main).launch {
            val ciphertext =
                binding.outputText.text.toString().replace("Зашифрованное сообщение: ", "")
            if (ciphertext.isNotEmpty()) {
                val message = rsa.decrypt(ciphertext)
                binding.outputText.setText(message)
                binding.outputText.visibility = View.VISIBLE
                binding.buttonCopy.isVisible = true
                binding.btnEncryptDecrypt.visibility = View.VISIBLE
            } else {
                generalFunctions.showShortToast(context, "Ключи не найдены", 200)
            }
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
                binding.fieldForKey.setHint(R.string.close_key)
                false
            }

            else -> {
                binding.btnEncryptDecrypt.setText(R.string.encrypt)
                binding.fieldForKey.setHint(R.string.open_key)
                true
            }
        }
    }
}











