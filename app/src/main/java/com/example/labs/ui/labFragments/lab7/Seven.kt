package com.example.labs.ui.labFragments.lab7

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var _binding: FragmentLab7Binding?= null
    private val binding: FragmentLab7Binding
        get() = _binding ?: throw RuntimeException("FragmentLab7Binding == null")

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
        _binding = FragmentLab7Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnApply.isEnabled = false
            login.addTextChangedListener(textWatcher)
            password.addTextChangedListener(textWatcher)

            btnApply.setOnClickListener {
                entryInSystem()
            }
            btnRegistration.setOnClickListener {
                findNavController().navigate(R.id.action_seven_to_registrationFragment)
            }
        }
    }

    private fun entryInSystem() {
        val login = binding.login.text.toString().trim()
        val password = binding.password.text.toString().trim()
        if (login != viewModel.login.toString() || password != viewModel.password.toString()) {
            generalFunctions.showShortToast(context, "Неправильный логин и/или пароль", 1000)
            return
        }
        else{
            showEntryInSystem()
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
}