package com.example.smartmessenger

import com.example.smartmessenger.model.Field

data class SignUpData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String
) {

    fun validate() {
        if (username.isBlank()) throw EmptyFieldException(Field.Username)
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
        if (password != repeatPassword) throw PasswordMismatchException(R.string.test_error.toString())
    }
}