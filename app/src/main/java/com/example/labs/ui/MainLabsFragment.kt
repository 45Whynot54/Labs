package com.example.labs.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.databinding.FragmentMainLabsBinding


open class MainLabsFragment : Fragment() {

    private val generalFunctions = GeneralFunctionsImpl

    private var _binding: FragmentMainLabsBinding ?= null
    val binding: FragmentMainLabsBinding
        get() = _binding ?: throw RuntimeException("MainLabsFragmentBinding == null")

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
        _binding = FragmentMainLabsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.inputText.addTextChangedListener(textWatcher)
        binding.fieldForKey.addTextChangedListener(textWatcher)
        binding.buttonCopy.setOnClickListener {
            generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
        }

        binding.backOnMain.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showExplanation(labId: String) {
        val bottomSheet = ExplanationBottomSheetFragment.newInstance(labId)
        bottomSheet.show(parentFragmentManager, "ExplanationBottomSheetFragment")
    }


    protected fun selectedOption(): Boolean {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.btn_encrypt -> true
            R.id.btn_decrypt -> false
            else -> throw IllegalArgumentException("Неправильный выбор!")
        }
    }

    protected fun updateHintForField(openKeyHint: Int, closeKeyHint: Int) {
        binding.fieldForKey.setHint(
            if (selectedOption()) {
                openKeyHint
            } else {
                closeKeyHint
            }
        )
    }

    private fun enableOrDisableButton() {
        val inputText = binding.inputText.text.toString()
        val shiftValue = binding.fieldForKey.text.toString()
        binding.btnEncryptOrDecrypt.isEnabled = inputText.isNotEmpty() && shiftValue.isNotEmpty()
    }
}