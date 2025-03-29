package com.example.labs.ui.labFragments.lab7

import androidx.lifecycle.ViewModel

class PasswordAndLoginVM: ViewModel() {

    var password: String ?= null ?: "hello"
    var login: String ?= null ?: "hello"
    var question: String ?= null ?: "hello"
    var answerOnQuestion: String ?= null ?: "hello"
    var timer: Int?= null

}