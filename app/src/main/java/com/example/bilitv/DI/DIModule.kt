package com.example.bilitv.DI

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.api.LiveApi
import com.jing.bilibilitv.http.api.PassportApi
import com.jing.bilibilitv.http.api.SearchApi
import com.jing.bilibilitv.http.api.wbi.WbiInterceptor
import com.jing.bilibilitv.http.cookie.BilibiliCookieJar
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DIModule {
    @Provides
    @Singleton
    fun provideCookieJar() = BilibiliCookieJar()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideOkHttpClient(cookieJar: BilibiliCookieJar): OkHttpClient {

        val builder = OkHttpClient
            .Builder()
            .cookieJar(cookieJar)
            .addInterceptor(WbiInterceptor())
            .addInterceptor(Interceptor { chain ->
                chain.request().newBuilder()
                    .header(
                        "user-agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36"
                    )
                    .header("referer", "https://www.bilibili.com")
                    .build()
                    .let {
                        chain.proceed(it)
                    }
            })
        return builder.build()
    }


    @Provides
    @Singleton
    fun provideLoginApi(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): PassportApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(PassportApi.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(PassportApi::class.java)


    @Provides
    @Singleton
    fun provideBilibiliApi(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): BilibiliApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BilibiliApi.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(BilibiliApi::class.java)


    @Provides
    @Singleton
    fun provideSearchApi(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): SearchApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(SearchApi.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(SearchApi::class.java)


    @Provides
    @Singleton
    fun provideLiveApi(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): LiveApi = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(LiveApi.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .build()
        .create(LiveApi::class.java)
}