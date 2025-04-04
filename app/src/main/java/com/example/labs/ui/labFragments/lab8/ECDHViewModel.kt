package com.example.labs.ui.labFragments.lab8

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.labs.data.lab8.Config
import com.example.labs.data.lab8.DiffieHellmanECDH
import com.example.labs.data.lab8.ECParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ECDHViewModel : ViewModel() {
    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val params: ECParameters = Config.getDefaultParams()

    fun calculateSharedSecret() {
        viewModelScope.launch(Dispatchers.Default) {
            try {
                val publicKeyA = DiffieHellmanECDH.generatePublicKey(params, true)
                val publicKeyB = DiffieHellmanECDH.generatePublicKey(params, false)
                val secretA = DiffieHellmanECDH.computeSharedSecret(params, true, publicKeyB)
                val secretB = DiffieHellmanECDH.computeSharedSecret(params, false, publicKeyA)

                _result.postValue(
                    if (secretA == secretB) "Общий секрет: ${secretA}"
                    else "Ошибка: секреты не совпадают"
                )
            } catch (e: Exception) {
                _result.postValue("Ошибка: ${e.message}")
            }
        }
    }
}