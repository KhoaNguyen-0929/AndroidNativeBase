/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vn.start.androidnativebase.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.start.data.BuildConfig
import vn.start.data.remote.APIServices
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Network module for providing Retrofit and OkHttp instances.
 * Moved to app module as workaround for KSP external type resolution limitation.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideAPIClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .addInterceptor(Interceptor { chain ->
                val ongoing: Request.Builder = chain.request().newBuilder().apply {
                    header("Accept", "application/json")
                    header("Content-Type", "application/json")
                }
                chain.proceed(ongoing.build())
            })
            .addNetworkInterceptor {
                val request: Request =
                    it.request().newBuilder().addHeader("Connection", "Close").build()
                it.proceed(request)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitApi(client: OkHttpClient): APIServices {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().create()
                )
            )
            .baseUrl(BuildConfig.SERVER_NORMAL_URL)
            .client(client)
            .build()
        return retrofit.create(APIServices::class.java)
    }
}
