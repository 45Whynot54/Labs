package com.example.labs.ui.labFragments.lab4

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.labs.data.GeneralFunctionsImpl
import com.example.labs.data.lab4.KeyStorage
import com.example.labs.data.lab4.RSAKeyGenerator
import com.example.labs.databinding.FragmentCreationKeyBottomSheetBinding
import com.example.labs.domain.GeneralFunctions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class CreationKeyBottomSheetFragment : BottomSheetDialogFragment() {

    private val generalFunctions = GeneralFunctionsImpl

    private var _binding: FragmentCreationKeyBottomSheetBinding? = null
    private val binding: FragmentCreationKeyBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentCreationKeyBottomSheetBinding == null")

    var onKeysGenerated: ((publicKey: String) -> Unit)? = null

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

        binding.btnGenerateKeys.setOnClickListener {
            val (n, e, d) = RSAKeyGenerator.generateKeys(25)
            KeyStorage(requireContext()).saveKeys(n, e, d)

            // Отображаем открытый ключ
            val publicKey = "${e}"
            val closeKey = "${d}"
            binding.openKey.text = publicKey
            binding.closeKey.text = closeKey

            // Передаем открытый ключ в основной интерфейс
            onKeysGenerated?.invoke(publicKey)
        }
        binding.closeKey.setOnClickListener {
            generalFunctions.showShortToast(context, "Зачем ты сюда нажал? :)", 200)
            showHiden()
        }
    }

    private fun showHiden() {
        //TODO()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
