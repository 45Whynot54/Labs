package com.example.labs.ui.labFragments.lab5

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.ui.GeneralViewModel
import com.example.labs.ui.MainLabsFragment

class Fifth: MainLabsFragment() {

    private val generalFunctions = GeneralFunctionsImpl
    private val viewModel: GeneralViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            textForNameLab.setText(R.string.name_for_lab5)
            fieldForKey.setHint(R.string.key)
            btnForBottomDialog.setText(R.string.create_key)
            btnForBottomDialog.setOnClickListener {
                showGenerateKey("Lab5")
            }
            fieldForKey.setInputType(InputType.TYPE_CLASS_NUMBER)

            btnEncryptOrDecrypt.setOnClickListener {
                encyptOrDecrypt()
            }
        }
    }

    private fun encyptOrDecrypt() {
        val message = binding.inputText.text.toString()
        if (viewModel.generatedKeyLab5 == null) {
            generalFunctions.showShortToast(requireContext(), "Сгенерируйте ключ", 1000)
            return
        }
        val key = viewModel.generatedKey.toString()
        val result = if (selectedOption()) {
            TODO()
        } else {
            TODO()
        }

        binding.outputText.text = result
        binding.outputText.isVisible = true
        binding.buttonCopy.isVisible = true

    }

}