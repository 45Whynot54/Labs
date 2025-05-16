package com.example.labs.ui.labFragments.lab5

import SignatureViewModel
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.labs.R
import com.example.labs.data.lab5.DigitalSignature
import com.example.labs.databinding.FifthLabFragmentBinding
import java.io.File

class Fifth : Fragment() {
    private var _binding: FifthLabFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignatureViewModel by viewModels()

    private val filePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.data?.let { uri ->
            try {
                // Получаем реальный файл через ContentResolver
                val inputStream = requireContext().contentResolver.openInputStream(uri)
                val file = createTempFileFromUri(uri)
                inputStream?.close()

                viewModel.setSelectedFile(file)
                binding.btnDownloadFile.text = file.name
                updateUI()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Ошибка открытия файла: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val saveFileLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        uri?.let {
            try {
                requireContext().contentResolver.openOutputStream(uri)?.use { output ->
                    val signatureContent = viewModel.getSignatureContent()
                    output.write(signatureContent?.toByteArray() ?: byteArrayOf())
                    Toast.makeText(context, "Подпись сохранена", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun createTempFileFromUri(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)!!
        val fileName = getFileNameFromUri(uri)
        val tempFile = File(requireContext().cacheDir, fileName ?: "temp_file")

        tempFile.outputStream().use { output ->
            inputStream.copyTo(output)
        }

        return tempFile
    }

    private fun getFileNameFromUri(uri: Uri): String? {
        return requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    cursor.getString(displayNameIndex)
                } else null
            } else null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FifthLabFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        with(binding) {
            fieldForKey.setInputType(InputType.TYPE_CLASS_TEXT)

            btnDownloadFile.setOnClickListener {
                openFilePicker()
            }


            btnForCheck.setOnClickListener {
                viewModel.verifySignature()
            }

            buttonApply.setOnClickListener {
                when (radioGroup.checkedRadioButtonId) {
                    R.id.btn_encrypt -> {
                        viewModel.signFile()
                        if (viewModel.shouldSaveSignature()) {
                        saveSignatureFile()
                    }
                }
                    R.id.btn_decrypt -> viewModel.verifySignature()
                }
                handleEncryptMode()
            }

            buttonCopy.setOnClickListener {
                copyToClipboard(outputText.text.toString())
            }

            backOnMain.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun handleEncryptMode() {
        with(binding) {
            fieldForKey.run {
                visibility = View.GONE
                text?.clear()
                hint = ""
                isEnabled = false
            }
        }
    }

    private fun handleDecryptMode() {
        with(binding) {
            fieldForKey.run {
                visibility = View.VISIBLE
                setText(DigitalSignature.publicKey.toString(16))
                hint = getString(R.string.public_key)
                inputType = InputType.TYPE_NULL
                isEnabled = false
            }
        }
    }

    private fun saveSignatureFile() {
        val baseName = viewModel.getOriginalFileName() ?: "signed_file"
        val fileName = "$baseName.sig"

        saveFileLauncher.launch(fileName)
    }

    private fun observeViewModel() {
        viewModel.signatureResult.observe(viewLifecycleOwner) { result ->
            binding.outputText.text = result
            binding.outputText.isVisible = true
            binding.buttonCopy.isVisible = true
        }

        viewModel.verificationResult.observe(viewLifecycleOwner) { result ->
            binding.outputText.text = result
            binding.outputText.isVisible = true
            binding.buttonCopy.isVisible = true
        }
    }

    private fun updateUI() {
        binding.buttonApply.isEnabled = viewModel.hasFile()
        binding.btnForCheck.isEnabled = viewModel.hasFile()
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        filePickerLauncher.launch(intent)
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Signature result", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}