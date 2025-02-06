package com.example.labs.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.labs.data.AllFunctionImpl
import com.example.labs.databinding.FragmentMainLabsBinding


abstract class MainLabsFragment(val context: Context) : Fragment() {

    protected fun setTitle(@StringRes stringResId: Int) {
        if (context is Activity) {
            context.title = context.getString(stringResId)
        }
    }
    private val generalFunctions = AllFunctionImpl

    var _binding: FragmentMainLabsBinding ?= null
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
        binding.symbolShift.addTextChangedListener(textWatcher)
        binding.buttonCopy.setOnClickListener {
            generalFunctions.copyText(requireContext(), binding.outputText.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun enableOrDisableButton() {
        val inputText = binding.inputText.text.toString()
        val shiftValue = binding.symbolShift.text.toString()
        binding.buttonApply.isEnabled = inputText.isNotEmpty() && shiftValue.isNotEmpty()
    }
}