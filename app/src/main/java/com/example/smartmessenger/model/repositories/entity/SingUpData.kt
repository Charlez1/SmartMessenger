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
        if (repeatPassword.isBlank()) throw EmptyFieldException(Field.RepeatPassword)
        if (password != repeatPassword) throw PasswordMismatchException(R.string.password_mismatch_error.toString())
    }
}