package com.asdepique.allbikestest.repositories

import com.asdepique.allbikestest.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor to add the JCDecaux API key to all HTTP calls
 */
class ApiKeyInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url

        val newUrl = originalHttpUrl.newBuilder()
            .addQueryParameter("apiKey", BuildConfig.JCDECAUX_API_KEY)
            .build()

        val newRequest = original.newBuilder().url(newUrl).build()

        return chain.proceed(newRequest);    }
}