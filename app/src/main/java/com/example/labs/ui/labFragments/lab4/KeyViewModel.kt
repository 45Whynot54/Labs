package com.example.labs.ui.labFragments.lab4

import androidx.lifecycle.ViewModel
import java.math.BigInteger

class KeyViewModel : ViewModel() {
    var publicKey: Pair<BigInteger, BigInteger>? = null
    var privateKey: Pair<BigInteger, BigInteger>? = null
}