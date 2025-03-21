package com.example.labs.ui.labFragments.lab6

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.labs.R
import com.example.labs.data.lab6.Steganography
import com.example.labs.databinding.SixthLabFragmentBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class Sixth : Fragment() {

    private var _binding: SixthLabFragmentBinding? = null
    private val binding get() = _binding!!

    private var selectedImage: Bitmap? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                loadImageFromUri(it)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SixthLabFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Обработка выбора режима (шифрование/дешифрование)
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioEncrypt -> binding.editTextMessage.visibility = View.VISIBLE
                R.id.radioDecrypt -> binding.editTextMessage.visibility = View.GONE
            }
            binding.editTextMessage.text.clear()
            binding.textViewDecryptedMessage.isVisible = false
        }

        // Обработка нажатия на кнопку "Загрузить изображение"
        binding.buttonLoadImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        // Обработка нажатия на кнопку "Применить"
        binding.buttonApply.setOnClickListener {
            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.radioEncrypt -> {
                    val message = binding.editTextMessage.text.toString()
                    if (message.isNotEmpty() && selectedImage != null) {
                        val encryptedImage = Steganography.embedMessage(selectedImage!!, message)
                        saveImageToGallery(encryptedImage)
                        binding.textViewDecryptedMessage.visibility = View.GONE
                        Toast.makeText(requireContext(), "Сообщение зашифровано и изображение сохранено", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Введите сообщение и выберите изображение", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.radioDecrypt -> {
                    if (selectedImage != null) {
                        val decryptedMessage = Steganography.extractMessage(selectedImage!!)
                        binding.editTextMessage.setText(decryptedMessage)
                        binding.textViewDecryptedMessage.text = "Расшифрованный текст: $decryptedMessage"
                        binding.textViewDecryptedMessage.visibility = View.VISIBLE
                        Toast.makeText(requireContext(), "Сообщение дешифровано", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Выберите изображение", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Загрузка изображения из Uri
    private fun loadImageFromUri(uri: Uri) {
        try {
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
            selectedImage = bitmap
            Toast.makeText(requireContext(), "Изображение загружено", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show()
        }
    }

    // Сохранение изображения в галерею
    private fun saveImageToGallery(bitmap: Bitmap) {
        val filename = "steganography_${System.currentTimeMillis()}.jpg"
        var outputStream: OutputStream? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Для Android 10 и выше
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val resolver = requireContext().contentResolver
            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                outputStream = resolver.openOutputStream(it)
            }
        } else {
            // Для Android 9 и ниже
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val imageFile = File(imagesDir, filename)
            outputStream = FileOutputStream(imageFile)
        }

        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(requireContext(), "Изображение сохранено в галерею", Toast.LENGTH_SHORT).show()
        } ?: run {
            Toast.makeText(requireContext(), "Ошибка при сохранении изображения", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}