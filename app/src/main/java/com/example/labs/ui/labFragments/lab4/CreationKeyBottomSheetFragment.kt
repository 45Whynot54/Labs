package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.RSAKeyGenerator
import com.example.labs.databinding.FragmentCreationKeyBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class CreationKeyBottomSheetFragment : BottomSheetDialogFragment() {

    private val generalFunctions = GeneralFunctionsImpl
    private val viewModel: KeyViewModel by activityViewModels()

    private var _binding: FragmentCreationKeyBottomSheetBinding? = null
    private val binding: FragmentCreationKeyBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentCreationKeyBottomSheetBinding == null")

    var onKeysGenerated: ((publicKey: String, closeKey: String) -> Unit)? = null

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            enableOrDisableButton()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreationKeyBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            openKey.setHint(R.string.created_open_key)
            closeKey.setHint(R.string.created_close_key)

            editTextNumberOfKeyCharacters.addTextChangedListener(textWatcher)

            viewModel.publicKey?.let { (_, e) ->
                openKey.text = e
            }

            viewModel.privateKey?.let { (_, d) ->
                closeKey.text = d
            }

            btnGenerateKeys.setOnClickListener {
                genSameLengthKeys()
            }
            openKey.setOnClickListener {
                generalFunctions.showShortToast(context, "Зачем ты сюда нажал? :)", 300)
            }

            openKey.setOnLongClickListener {
                generalFunctions.copyText(requireContext(), openKey.text.toString())
                openKey.isClickable = true
                true
            }
            closeKey.setOnClickListener {
                generalFunctions.showShortToast(context, "Зачем ты сюда нажал? :)", 300)
                showHiden()
            }

            closeKey.setOnLongClickListener {
                generalFunctions.copyText(requireContext(), closeKey.text.toString())
                openKey.isClickable = true
                true
            }
        }
    }

    private fun genSameLengthKeys() {
        val countCharacters = binding.editTextNumberOfKeyCharacters.text.toString().toIntOrNull() ?: 1
        val bitLengthForKey = countCharacters * 4
        if (bitLengthForKey > 1024) {
            generalFunctions.showShortToast(requireContext(), "Введите количество символов для ключа не более 256", 500)
            return
        } else {
            viewModel.countBitLength = bitLengthForKey
        }
        do {
            val (nHex, eHex, dHex) = RSAKeyGenerator.generateKeys(bitLengthForKey)
            viewModel.publicKey = Pair(nHex, eHex)
            viewModel.privateKey = Pair(nHex, dHex)
            viewModel.updateButtonState()
            binding.openKey.text = eHex
            binding.closeKey.text = dHex
            onKeysGenerated?.invoke(eHex, dHex)

        } while (eHex.length != dHex.length)
        val keysSize = binding.openKey.text.toString().length
        generalFunctions.showShortToast(context, "Длина ключей: $keysSize символов", 100)
    }

    private fun showHiden() {
        //TODO()
    }

    private fun enableOrDisableButton() {
        val countField = binding.editTextNumberOfKeyCharacters.text.toString()
        binding.btnGenerateKeys.isEnabled = countField.isNotEmpty()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

