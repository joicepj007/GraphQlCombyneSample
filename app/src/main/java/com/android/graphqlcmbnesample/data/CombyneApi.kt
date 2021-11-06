package com.android.graphqlcmbnesample.data

import android.os.Looper
import com.android.graphqlcmbnesample.util.Constants.APPLICATION_ID
import com.android.graphqlcmbnesample.util.Constants.APPLICATION_VALUE
import com.android.graphqlcmbnesample.util.Constants.BASE_URL
import com.android.graphqlcmbnesample.util.Constants.CLIENT_KEY
import com.android.graphqlcmbnesample.util.Constants.CLIENT_VALUE
import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request

class CombyneApi {

    fun getApolloClient(): ApolloClient {
        check(Looper.myLooper() == Looper.getMainLooper()) {
            "Only the main thread can get the apolloClient instance"
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val original: Request = chain.request()
                val builder: Request.Builder =
                    original.newBuilder().method(original.method(), original.body())
                builder.header(CLIENT_KEY, CLIENT_VALUE)
                builder.header(APPLICATION_ID, APPLICATION_VALUE)
                chain.proceed(builder.build())
            }
            .build()
        return ApolloClient.builder()
            .serverUrl(BASE_URL)
            .okHttpClient(okHttpClient)
            .build()

    }
}