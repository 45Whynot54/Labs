package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.labs.databinding.FourthLabFragmentBinding

class Fourth: Fragment() {


    private var _binding: FourthLabFragmentBinding?= null
    val binding: FourthLabFragmentBinding
        get() = _binding ?: throw RuntimeException("FourthLabFragmentBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FourthLabFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createKeyBtn.setOnClickListener {
            showExplanation()
        }
    }

    private fun showExplanation() {
        val bottomSheet = CreationKeyBottomSheetFragment()
        bottomSheet.show(parentFragmentManager, "CreationKeyBottomSheetFragment")
    }
}