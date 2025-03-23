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
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.databinding.FragmentLab7Binding
import com.example.labs.databinding.FragmentMainLabsBinding
import com.example.labs.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding
        get() = _binding ?: throw RuntimeException("FragmentRegistrationBinding == null")

    private val viewModel: PasswordAndLoginVM by activityViewModels()
    private val generalFunctions = GeneralFunctionsImpl

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
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.addTextChangedListener(textWatcher)
        binding.button.setOnClickListener {
            registration()
        }
    }

    private fun registration() {

        val password = binding.passwordReg.text.toString()
        val retryPassword = binding.retryPasswordReg.text.toString()
        if (password == retryPassword) {
            viewModel.login = binding.loginReg.text.toString()
            viewModel.password = binding.passwordReg.text.toString()
            viewModel.question = binding.questionReg.text.toString()
            viewModel.answerOnQuestion = binding.answerOnQuestionReg.text.toString()
        } else {
            generalFunctions.showShortToast(context, "Пароли не совпадают", 1000)
        }
    }

    private fun enableOrDisableButton() {
        val login = binding.loginReg.text.toString()
        val password = binding.passwordReg.text.toString()
        val retryPassword = binding.retryPasswordReg.text.toString()
        val question = binding.questionReg.text.toString()
        val answer = binding.answerOnQuestionReg.text.toString()
        binding.button.isEnabled =
            login.isNotEmpty() &&
                    password.isNotEmpty() &&
                    retryPassword.isNotEmpty() &&
                    question.isNotEmpty() &&
                    answer.isNotEmpty()
    }
}