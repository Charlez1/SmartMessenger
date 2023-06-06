package com.hfad.fitness.async


sealed class Result<T> {
    public fun getSuccess(): T? {
        if (this is SuccessResult)
            return this.data
        else
            return null
    }
}

class SuccessResult<T> (
    val data: T
) : Result<T>()

class ErrorResult<T>(
    val exception: Exception
) : Result<T>()

class PendingResult<T> : Result<T>()