package com.example.labs.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
        binding.btnForCheck.isVisible = false
        binding.explanationImageView.setOnClickListener {
            showExplanation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun updateButtonConstraints(btnForCheckVisible: Boolean) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.root)

        if (btnForCheckVisible) {
            constraintSet.connect(
                R.id.btn_for_bottom_dialog,
                ConstraintSet.END,
                R.id.btn_for_check,
                ConstraintSet.START,
                resources.getDimensionPixelSize(R.dimen.button_margin)
            )
            constraintSet.connect(
                R.id.btn_for_check,
                ConstraintSet.START,
                R.id.btn_for_bottom_dialog,
                ConstraintSet.END,
                resources.getDimensionPixelSize(R.dimen.button_margin)
            )
            constraintSet.setHorizontalWeight(R.id.btn_for_bottom_dialog, 1f)
            constraintSet.setHorizontalWeight(R.id.btn_for_check, 1f)
        } else {
            constraintSet.connect(
                R.id.btn_for_bottom_dialog,
                ConstraintSet.END,
                ConstraintSet.PARENT_ID,
                ConstraintSet.END,
                0
            )
            constraintSet.clear(R.id.btn_for_check, ConstraintSet.START)
            constraintSet.setHorizontalWeight(R.id.btn_for_bottom_dialog, 1f)
        }
        constraintSet.applyTo(binding.root)
    }


    protected fun showExplanation() {
        val bottomSheet = ExplanationBottomSheetFragment()
        bottomSheet.show(parentFragmentManager, "ExplanationBottomSheetFragment")
    }


    protected fun selectedOption(): Boolean {
        return when (binding.radioGroup.checkedRadioButtonId) {
            R.id.btn_encrypt -> true
            R.id.btn_decrypt -> false
            else -> throw IllegalArgumentException("Неправильный выбор!")
        }
    }

    protected fun updateTextForButton(){
        binding.btnEncryptOrDecrypt.setText(
            if (selectedOption()){
                R.string.encrypt
            } else {
                R.string.decrypt
            }
        )
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