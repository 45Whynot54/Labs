package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.RSAKeyGenerator
import com.example.labs.databinding.FragmentCreationKeyBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.math.BigInteger


open class CreationKeyBottomSheetFragment : BottomSheetDialogFragment() {

    private val generalFunctions = GeneralFunctionsImpl
    private val viewModel: KeyViewModel by activityViewModels()

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

        viewModel.openKey?.let { openKey ->
            binding.openKey.text = openKey
        }
        viewModel.closeKey?.let { closeKey ->
            binding.closeKey.text = closeKey
        }

        binding.btnGenerateKeys.setOnClickListener {

            val (openKey: String, closeKey: String) = regenerateKeys()

            binding.openKey.text = openKey
            binding.closeKey.text = closeKey

            viewModel.openKey = openKey
            viewModel.closeKey = closeKey

            onKeysGenerated?.invoke(openKey, closeKey)

            val keysSize = binding.openKey.text.toString().length
            generalFunctions.showShortToast(context, "Длина ключей: ${keysSize} символов", 300)

        }

        binding.closeKey.setOnClickListener {
            generalFunctions.showShortToast(context, "Зачем ты сюда нажал? :)", 200)
            showHiden()
        }

        binding.openKey.isClickable = false
    }

    private fun regenerateKeys(): Pair<String, String> {
        var openKey: String
        var closeKey: String
        var n: BigInteger
        var e: BigInteger
        var d: BigInteger

        do {
            val keys = RSAKeyGenerator.generateKeys(48)
            n = keys.first
            e = keys.second
            d = keys.third

            openKey = "${e}"
            closeKey = "${d}"

        } while (openKey.length != closeKey.length || openKey.length > 14 || openKey.length < 13)
        return Pair(openKey, closeKey)
    }

    private fun showHiden() {
        //TODO()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

