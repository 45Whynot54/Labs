package com.example.labs.ui.labFragments.lab4

import androidx.lifecycle.ViewModel
import java.math.BigInteger

class KeyViewModel : ViewModel() {
    var publicKey: Pair<String, String>? = null
    var privateKey: Pair<String, String>? = null
}