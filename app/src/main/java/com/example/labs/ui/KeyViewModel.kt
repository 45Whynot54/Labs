package com.example.labs.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class KeyViewModel : ViewModel() {

    //lab3
    var generatedKey: String?= null

    //lab4
    var publicKey: Pair<String, String>? = null
    var privateKey: Pair<String, String>? = null
    var countBitLength: Int ?= null
    var checkOpenKey: Pair<String, String>? = null

    private val _isButtonEnabled = MutableLiveData<Boolean>()
    val isButtonEnabled: LiveData<Boolean> get() = _isButtonEnabled

    fun updateButtonState() {
        _isButtonEnabled.value = publicKey != null && privateKey != null
    }

    //lab 5
    var generatedKeyLab5: String?= null
}