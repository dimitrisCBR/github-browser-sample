package xyz.cbrlabs.githubbrowsersample.network

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import xyz.cbrlabs.githubbrowsersample.network.model.ApiResponse
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Register this adapter with your Retrofit Builder if you want to use [ApiResponse]
 * to handle success, error or empty response on the network layer.
 */
class ApiResponseCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Call<ApiResponse<T>>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<T>): Call<ApiResponse<T>> {
        return ApiResponseCall(call)
    }
}

class ApiResponseCall<T>(
    private val delegate: Call<T>
) : Call<ApiResponse<T>> {

    override fun enqueue(callback: retrofit2.Callback<ApiResponse<T>>) {
        delegate.enqueue(object : retrofit2.Callback<T> {
            override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                val apiResponse = ApiResponse.create(response)
                callback.onResponse(this@ApiResponseCall, retrofit2.Response.success(apiResponse))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiErrorResponse = ApiResponse.create<T>(t)
                callback.onResponse(this@ApiResponseCall, retrofit2.Response.success(apiErrorResponse))
            }
        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<ApiResponse<T>> = ApiResponseCall(delegate.clone())

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    override fun cancel() = delegate.cancel()

    override fun execute(): retrofit2.Response<ApiResponse<T>> {
        throw UnsupportedOperationException("ApiResponseCall doesn't support execute()")
    }
}

/**
 * Register this CallAdapter.Factory with your Retrofit client to use [ApiResponseCallAdapter]
 * Usage:
 *  Retrofit.Builder()
 *  ...
 *  .addCallAdapterFactory(ApiResponseConverterFactory())
 */
class ApiResponseConverterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        // suspend functions wrap the response type in `Call`
        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        // check first that the return type is `ParameterizedType`
        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<NetworkResponse<<Foo>> or Call<NetworkResponse<out Foo>>"
        }

        // get the response type inside the `Call` type
        val responseType = getParameterUpperBound(0, returnType)
        // if the response type is not ApiResponse then we can't handle this type, so we return null
        if (getRawType(responseType) != ApiResponse::class.java) {
            return null
        }

        // the response type is ApiResponse and should be parameterized
        check(responseType is ParameterizedType) { "Response must be parameterized as ApiResponse<Foo> or ApiResponse<out Foo>" }

        val successBodyType = getParameterUpperBound(0, responseType)

        return ApiResponseCallAdapter<Any>(successBodyType)
    }
}