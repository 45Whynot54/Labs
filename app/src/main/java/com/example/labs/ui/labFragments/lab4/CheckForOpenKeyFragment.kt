package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.RSAImpl
import com.example.labs.databinding.CheckOpenKeyFragmentBinding
import com.example.labs.ui.GeneralViewModel

class CheckForOpenKeyFragment : Fragment() {

    private val viewModel: GeneralViewModel by activityViewModels()
    private val generalFunctions = GeneralFunctionsImpl
    private var rsa = RSAImpl

    private var _binding: CheckOpenKeyFragmentBinding? = null
    private val binding: CheckOpenKeyFragmentBinding
        get() = _binding ?: throw RuntimeException("CheckOpenKeyFragmentBinding == null")

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
        _binding = CheckOpenKeyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnApply.setOnClickListener {
                check()
            }
            fieldForEncryptMessage.addTextChangedListener(textWatcher)
            fieldForOpenKey.addTextChangedListener(textWatcher)
            backOnMain.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun check() {
        try {
            val ciphertext = binding.fieldForEncryptMessage.text.toString()
            val n = viewModel.publicKey!!.first
            val openKey = binding.fieldForOpenKey.text.toString()
            viewModel.checkOpenKey = Pair(n, openKey)
            val result = viewModel.checkOpenKey
            val message = rsa.decrypt(ciphertext, result!!)
            binding.outputText.text = message
            binding.outputText.isVisible = true
            binding.buttonCopy.isVisible = true
        } catch (e:Exception){
            generalFunctions.showShortToast(requireContext(), "Ошибка в вычислениях....", 500)
            Handler(Looper.getMainLooper()).postDelayed({
                generalFunctions.showShortToast(requireContext(), "Введите открытый ключ и", 500)
            }, 1000)

            Handler(Looper.getMainLooper()).postDelayed({
                generalFunctions.showShortToast(requireContext(), "зашифрованное сообщение", 500)
            }, 2000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun enableOrDisableButton() {
        val inputText = binding.fieldForOpenKey.text.toString()
        val shiftValue = binding.fieldForEncryptMessage.text.toString()
        binding.btnApply.isEnabled = inputText.isNotEmpty() && shiftValue.isNotEmpty()
    }
}
