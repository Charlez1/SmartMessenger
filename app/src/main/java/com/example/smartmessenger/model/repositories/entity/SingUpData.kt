package com.example.smartmessenger.model.repositories.entity

import com.example.smartmessenger.EmptyFieldException
import com.example.smartmessenger.PasswordMismatchException
import com.example.smartmessenger.R
import com.example.smartmessenger.TooShortUsernameException
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
        if (username.length < 6) throw TooShortUsernameException(R.string.username_too_short_error.toString())
        if (username[0] != '@') throw IllegalArgumentException(R.string.username_illegal_argument_error.toString())
        if (password != repeatPassword) throw PasswordMismatchException(R.string.password_mismatch_error.toString())
    }
}