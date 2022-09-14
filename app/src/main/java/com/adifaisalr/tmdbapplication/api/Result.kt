package com.adifaisalr.tmdbapplication.api

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.io.Serializable

sealed class Result<out T> : Serializable {
    object NoData : Result<Nothing>()
    data class Success<T>(val data: T?) : Result<T>()
    data class Failure(val errorData: ErrorData) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object NetworkError : Result<Nothing>()

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

abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    final override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    final override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled
    override fun timeout(): Timeout = proxy.timeout()

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
}

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, Result<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<Result<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in 200 until 300) {
                val body = response.body()
                val successResult: Result<T> = Result.Success(body)
                successResult
            } else {
                Result.Failure(ErrorData(response.message(), code))
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = if (t is IOException) {
                Result.NetworkError
            } else {
                Result.Failure(ErrorData(t.toString(), 0))
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone())
}
