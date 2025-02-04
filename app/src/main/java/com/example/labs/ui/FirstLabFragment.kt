package com.example.labs.ui
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.labs.R
import com.example.labs.data.AllFunctionImpl
import com.example.labs.data.lab1.CaesarCipherImpl
import com.example.labs.databinding.FragmentFirstLabBinding


class FirstLabFragment : Fragment() {

    private val cipher = CaesarCipherImpl
    private val generalFunctions = AllFunctionImpl

    private var _binding: FragmentFirstLabBinding?= null
    private val binding: FragmentFirstLabBinding
        get() = _binding ?: throw RuntimeException("HomeFragmentBinding == null")

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
        _binding = FragmentFirstLabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputText.addTextChangedListener(textWatcher)
        binding.symbolShift.addTextChangedListener(textWatcher)
        binding.buttonApply.setOnClickListener {
            applyCaesarCipher()
        }
        binding.buttonCopy.setOnClickListener {
            generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun enableOrDisableButton() {
        binding.buttonApply.isVisible = true
        val inputText = binding.inputText.text.toString()
        val shiftValue = binding.symbolShift.text.toString()
        binding.buttonApply.isEnabled = inputText.isNotEmpty() && shiftValue.isNotEmpty()
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
    }
}

