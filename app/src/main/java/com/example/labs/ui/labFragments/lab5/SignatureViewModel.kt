import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.math.BigInteger

class SignatureViewModel(application: Application) : AndroidViewModel(application) {
    private val digitalSignature = com.example.labs.data.lab5.DigitalSignature

    private val _signatureResult = MutableLiveData<String>()
    val signatureResult: LiveData<String> = _signatureResult

    private val _verificationResult = MutableLiveData<String>()
    val verificationResult: LiveData<String> = _verificationResult

    private var currentFile: File? = null
    private var currentSignature: Pair<BigInteger, BigInteger>? = null

    fun hasFile(): Boolean = currentFile != null

    fun setSelectedFile(file: File) {
        currentFile = file
        currentSignature = null
    }

    fun getSignatureContent(): String? {
        return currentSignature?.let { (r, s) ->
            "Digital Signature\nR: ${r.toString(16)}\nS: ${s.toString(16)}"
        }
    }

    fun shouldSaveSignature(): Boolean {
        return currentSignature != null
    }

    fun getOriginalFileName(): String? {
        return currentFile?.name
    }

    fun signFile() {
        currentFile?.let { file ->
            try {
                currentSignature = digitalSignature.signFile(file)
                _signatureResult.value =
                    "${currentSignature?.first}, ${currentSignature?.second}"
            } catch (e: Exception) {
                _signatureResult.value = "Ошибка подписи: ${e.message}"
            }
        } ?: run {
            _signatureResult.value = "Файл не выбран"
        }
    }

    fun verifySignature() {
        currentFile?.let { file ->
            currentSignature?.let { (r, s) ->
                try {
                    val isValid = digitalSignature.verifySignature(file, r, s)
                    _verificationResult.value = if (isValid) {
                        "Подпись действительна. Файл не изменён."
                    } else {
                        "Подпись недействительна или файл изменён!"
                    }
                } catch (e: Exception) {
                    _verificationResult.value = "Ошибка проверки: ${e.message}"
                }
            } ?: run {
                _verificationResult.value = "Подпись отсутствует"
            }
        } ?: run {
            _verificationResult.value = "Файл не выбран"
        }
    }
}