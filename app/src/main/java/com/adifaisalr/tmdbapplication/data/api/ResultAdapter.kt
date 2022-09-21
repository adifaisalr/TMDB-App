package com.adifaisalr.tmdbapplication.data.api

import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.model.dataholder.ErrorData
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ResultAdapter(
    private val type: Type
) : CallAdapter<Type, Call<DataHolder<Type>>> {
    override fun responseType() = type
    override fun adapt(call: Call<Type>): Call<DataHolder<Type>> = ResultCall(call)
}

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

class ResultCall<T>(proxy: Call<T>) : CallDelegate<T, DataHolder<T>>(proxy) {
    override fun enqueueImpl(callback: Callback<DataHolder<T>>) = proxy.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in 200 until 300) {
                val body = response.body()
                body?.let {
                    val successResult: DataHolder<T> = DataHolder.Success(it)
                    successResult
                } ?: DataHolder.Failure(ErrorData(response.message(), code))
            } else {
                DataHolder.Failure(ErrorData(response.message(), code))
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = if (t is IOException) {
                DataHolder.NetworkError
            } else {
                DataHolder.Failure(ErrorData(t.toString(), 0))
            }

            callback.onResponse(this@ResultCall, Response.success(result))
        }
    })

    override fun cloneImpl() = ResultCall(proxy.clone())
}

class MyCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(0, returnType as ParameterizedType)
            when (getRawType(callType)) {
                DataHolder::class.java -> {
                    val resultType = getParameterUpperBound(0, callType as ParameterizedType)
                    ResultAdapter(resultType)
                }
                else -> null
            }
        }
        else -> null
    }
}