package br.com.painelb.network.adapter

import br.com.painelb.api.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.io.IOException
import java.lang.reflect.Type

class SynchronousCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, ApiResponse<R>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): ApiResponse<R> = try {
        ApiResponse.create(call.execute())
    } catch (e: IOException) {
        ApiResponse.create(e)
    }
}
