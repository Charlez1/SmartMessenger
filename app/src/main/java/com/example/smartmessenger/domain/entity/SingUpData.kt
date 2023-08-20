package com.example.smartmessenger.domain.entity

import com.example.smartmessenger.utils.EmptyFieldException
import com.example.smartmessenger.utils.PasswordMismatchException
import com.example.smartmessenger.R
import com.example.smartmessenger.utils.TooShortUsernameException
import com.example.smartmessenger.utils.Field

data class SignUpData(
    val uniqueName: String,
    val email: String,
    val password: String,
    val repeatPassword: String
) {

    fun validate() {
        if (uniqueName.isBlank()) throw EmptyFieldException(Field.Username)
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
        if (repeatPassword.isBlank()) throw EmptyFieldException(Field.RepeatPassword)
        if (uniqueName.length < 6) throw TooShortUsernameException(R.string.username_too_short_error.toString())
        if (uniqueName[0] != '@') throw IllegalArgumentException(R.string.username_illegal_argument_error.toString())
        if (password != repeatPassword) throw PasswordMismatchException(R.string.password_mismatch_error.toString())
    }
}