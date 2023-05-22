package com.example.smartmessenger

import com.example.smartmessenger.model.Field

open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class EmptyFieldException(
    val field: Field
) : AppException()

class AuthWeakPasswordException(message: String) : AppException(message = message)

class PasswordMismatchException(message: String) : AppException(message = message)

class AccountAlreadyExistsException(message: String) : AppException(message = message)

class AuthException(message: String) : AppException(message = message)

class InvalidUserException(message: String) : AppException(message = message)

class ConnectionException(message: String) : AppException(message = message)

class BackendException(message: String) : AppException(message = message)

class TooManyRequestsException(message: String) : AppException(message = message)

class AuthRecentLoginRequiredException(message: String) : AppException(message = message)

class UnknownException(message: String) : AppException(message = message)
