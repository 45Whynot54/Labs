package com.example.labs.ui.labFragments.lab4

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KeyViewModel : ViewModel() {
    var publicKey: Pair<String, String>? = null
    var privateKey: Pair<String, String>? = null
    var countBitLength: Int ?= null
    var checkOpenKey: Pair<String, String>? = null

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> get() = _isButtonEnabled

    fun updateButtonState() {
        _isButtonEnabled.value = publicKey != null && privateKey != null
    }
}