package com.frankegan.plantswap.extensions

inline fun <R: Any, T: Any> Result<T>.flatMap(transform: (data: T) -> Result<R>): Result<R> {
    return when {
        isSuccess -> transform(getOrThrow())
        else -> Result.failure(exceptionOrNull()!!)
    }
}