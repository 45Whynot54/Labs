package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.labs.R
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.RSAKeyGenerator
import com.example.labs.databinding.FragmentCreationKeyBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Random


open class CreationKeyBottomSheetFragment : BottomSheetDialogFragment() {

    private val generalFunctions = GeneralFunctionsImpl
    private val viewModel: KeyViewModel by activityViewModels()
    private var keysGeneratore = RSAKeyGenerator

    private var _binding: FragmentCreationKeyBottomSheetBinding? = null
    private val binding: FragmentCreationKeyBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentCreationKeyBottomSheetBinding == null")

    var onKeysGenerated: ((publicKey: String, closeKey: String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreationKeyBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            openKey.setHint(R.string.created_open_key)
            closeKey.setHint(R.string.created_close_key)

            viewModel.publicKey?.let { (_, e) ->
                openKey.text = e.toString()
            }

            viewModel.privateKey?.let { (_, d) ->
                closeKey.text = d.toString()
            }

            btnGenerateKeys.setOnClickListener {
                genSameLengthKeys()
            }
            openKey.setOnClickListener {
                generalFunctions.showShortToast(context, "Зачем ты сюда нажал? :)", 300)
            }

            openKey.setOnLongClickListener {
                generalFunctions.copyText(requireContext(), openKey.text.toString())
                openKey.isClickable = true
                true
            }
            closeKey.setOnClickListener {
                generalFunctions.showShortToast(context, "Зачем ты сюда нажал? :)", 300)
                showHiden()
            }

            closeKey.setOnLongClickListener {
                generalFunctions.copyText(requireContext(), closeKey.text.toString())
                openKey.isClickable = true
                true
            }
        }
    }

    private fun genSameLengthKeys() {
        do {
            val (n, e, d) = keysGeneratore.generateKeys(bitLength = 84)
            viewModel.publicKey = Pair(n, e)
            viewModel.privateKey = Pair(n, d)
            val publicKey = e.toString()
            val closeKey = d.toString()
            binding.openKey.text = publicKey
            binding.closeKey.text = closeKey
            onKeysGenerated?.invoke(e.toString(), d.toString())

        } while (publicKey.length != closeKey.length || publicKey.length > 26 || publicKey.length < 25)
        val keysSize = binding.openKey.text.toString().length
        generalFunctions.showShortToast(context, "Длина ключей: ${keysSize} символов", 100)
    }

    private fun showHiden() {
        //TODO()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

