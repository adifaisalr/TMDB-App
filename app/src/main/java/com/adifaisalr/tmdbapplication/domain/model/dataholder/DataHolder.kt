package com.adifaisalr.tmdbapplication.domain.model.dataholder

import java.io.Serializable

sealed class DataHolder<out T> : Serializable {
    object NoData : DataHolder<Nothing>()
    data class Success<T>(val data: T) : DataHolder<T>()
    data class Failure(val errorData: ErrorData) : DataHolder<Nothing>()
    object Loading : DataHolder<Nothing>()
    object NetworkError : DataHolder<Nothing>()

    /**
     * Peeking the data within this Result class.
     * Only returns the data when the Result is Success.
     */
    val peekData: T?
        get() = when (this) {
            is Success -> data
            else -> null
        }

    /**
     * Peeking the error data within this Result class.
     * Only returns the error data when the Result is Failure.
     */
    val peekError: Throwable?
        get() = when (this) {
            is Failure -> errorData
            else -> null
        }
}

data class ErrorData(
    override val message: String,
    val code: Int
) : Throwable(message)
