package br.com.painelb.network.adapter

import br.com.painelb.api.ApiResponse
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class SynchronousCallAdapterFactory : Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != ApiResponse::class.java) return null
        if (returnType !is ParameterizedType)
            throw IllegalArgumentException("resource must be parameterized")
        val responseType = getParameterUpperBound(0, returnType)
        return SynchronousCallAdapter<Any>(responseType)
    }
}
