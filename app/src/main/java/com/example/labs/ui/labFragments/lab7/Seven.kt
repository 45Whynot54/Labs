package com.example.labs.ui.labFragments.lab7

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.databinding.BottomEntryInSystemBinding
import com.example.labs.databinding.FragmentLab7Binding
import com.example.labs.databinding.HomeFragmentBinding
import com.example.labs.ui.GeneralViewModel
import com.example.labs.ui.GenerateKey

class Seven : Fragment() {

    private var _binding: FragmentLab7Binding? = null
    private val binding: FragmentLab7Binding
        get() = _binding ?: throw RuntimeException("FragmentLab7Binding == null")

    private val viewModel: PasswordAndLoginVM by activityViewModels()

    private val generalFunctions = GeneralFunctionsImpl
    private var isForgotMode: Boolean = false

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
        _binding = FragmentLab7Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            backOnMain.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            btnApply.isEnabled = false
            login.addTextChangedListener(textWatcher)
            password.addTextChangedListener(textWatcher)

            btnApply.setOnClickListener {
                entryInSystem()
            }
            btnRegistration.setOnClickListener {
                findNavController().navigate(R.id.action_seven_to_registrationFragment)
            }
            btnFoggot.setOnClickListener {
                changeType()
            }
        }
    }

    private fun entryInSystem() {
        val login = binding.login.text.toString().trim()
        val input = binding.password.text.toString().trim()

        with(viewModel) {

            if (isForgotMode) {
                when {
                    login != this.login -> generalFunctions.showShortToast(context, "Неверный логин", 1000)
                    input.isEmpty() -> generalFunctions.showShortToast(context, "Введите ответ", 1000)
                    input != answerOnQuestion -> generalFunctions.showShortToast(context, "Неверный ответ", 1000)
                    else -> showEntryInSystem()
                }
            } else {
                when {
                    login != this.login -> generalFunctions.showShortToast(context, "Неверный логин", 1000)
                    input != password -> generalFunctions.showShortToast(context, "Неверный пароль", 1000)
                    else -> showEntryInSystem()
                }
            }
        }
    }


    private fun showEntryInSystem() {
        val bottomSheetOne = EntryInSystemFragment()
        bottomSheetOne.show(parentFragmentManager, "EntryInSystemFragment")
    }

    private fun enableOrDisableButton() {
        val login = binding.login.text.toString()
        val password = binding.password.text.toString()
        binding.btnApply.isEnabled = login.isNotEmpty() && password.isNotEmpty()
    }

    private fun changeType() {
        isForgotMode = !isForgotMode
        with(binding) {
            question.isVisible = isForgotMode
            password.text?.clear()

            if (isForgotMode) {
                question.text = "${getString(R.string.your_question)} ${viewModel.question}"
                btnFoggot.text = getString(R.string.remember_password)
                password.hint = getString(R.string.answer)
            } else {
                btnFoggot.text = getString(R.string.foggot_password)
                password.hint = getString(R.string.password)
            }
        }
    }
}