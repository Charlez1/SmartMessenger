package com.example.smartmessenger

import com.example.smartmessenger.model.Field

data class SignUpData(
    val email: String,
    val password: String,
    val repeatPassword: String
) {

    fun validate() {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
        if (password != repeatPassword) throw PasswordMismatchException(R.string.test_error.toString())
    }
}