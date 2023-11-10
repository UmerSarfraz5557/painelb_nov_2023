package br.com.painelb.network.token

import br.com.painelb.prefs.PreferenceStorage
import br.com.painelb.util.BEARER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor(private val storage: PreferenceStorage) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", storage.token.BEARER_TOKEN)
            .url(originalHttpUrl)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}