package com.example.labs.ui.labFragments.lab7

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.databinding.BottomEntryInSystemBinding
import com.example.labs.databinding.FragmentGenerateKeyBottomDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class EntryInSystemFragment : BottomSheetDialogFragment() {

    private var _binding: BottomEntryInSystemBinding ?= null
    private val binding: BottomEntryInSystemBinding
        get() = _binding ?: throw RuntimeException("BottomEntryInSystemBinding == null")
    private val viewModel: PasswordAndLoginVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomEntryInSystemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            loginOpen.text = viewModel.login
            passwordOpen.text = viewModel.password
            btnClose.setOnClickListener {
                dismiss()
            }
        }
    }
}