package com.example.smartmessenger.utils

open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class EmptyFieldException(
    val field: Field
) : AppException()

class TooShortUsernameException(message: String) : AppException(message)

class NonUniqueNameException(message: String) : AppException(message)

// sign up exception
class AuthWeakPasswordException(message: String) : AppException(message = message)
class PasswordMismatchException(message: String) : AppException(message = message)
class AccountAlreadyExistsException(message: String) : AppException(message = message)


// overall auth exception
class InvalidUserException(message: String) : AppException(message = message)
class AuthException(message: String) : AppException(message = message)

// account database exception


// other database exception
class ConnectionException(message: String) : AppException(message = message)
class BackendException(message: String) : AppException(message = message)
class TooManyRequestsException(message: String) : AppException(message = message)

class AuthRecentLoginRequiredException(message: String) : AppException(message = message)//TODO?

class UnknownException(message: String) : AppException(message = message)
