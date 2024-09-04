package com.easybuilder.common.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitClient
 * Created by sky.Ren on 2024/9/4.
 * Description: Retrofit实例
 */
object RetrofitClient {
    /**
     * okhttpClient实例
     */
    private val okhttpClient:OkHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    /**
     * Retrofit实例
     */
    val retrofit:Retrofit by lazy {
        Retrofit.Builder()
            .client(okhttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NetConstant.BASE_URL)
            .build()
    }

    /**
     * 获取Retrofit实例
     */
    public fun getInstance(url:String): Retrofit? {
       return url.let {
           Retrofit.Builder()
               .client(okhttpClient)
               .addConverterFactory(GsonConverterFactory.create())
               .baseUrl(it)
               .build()
       }
    }

    /**
     * 创建服务
     */
    inline fun <reified T> createService():T = retrofit.create(T::class.java)

    inline fun <reified T> createService(url:String):T = getInstance(url)!!.create(T::class.java)
}