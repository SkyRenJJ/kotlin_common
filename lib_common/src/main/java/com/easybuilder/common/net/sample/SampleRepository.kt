package com.easybuilder.common.net.sample

import com.easybuilder.common.net.RetrofitClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

/**
 * SampleRepository
 * Created by sky.Ren on 2024/9/4.
 * Description: 测试demo
 */
interface ApiService {
    @GET("/FRwK4ja2fc753bd4e3704d2b368b5bbe0104e39e0835f1c/bn/api/1.0/auth/oauth/token?branchID=0")
    suspend fun test(): TestBean
}
//https://mockapi.eolink.com/FRwK4ja2fc753bd4e3704d2b368b5bbe0104e39e0835f1c/bn/api/1.0/auth/oauth/token?branchID=0
class SampleRepository {
    val client:ApiService by lazy {
        RetrofitClient.createService<ApiService>("https://mockapi.eolink.com/")
    }

    suspend fun test(block:(String)->Unit) {
        val test:TestBean = client.test()
        block(test.message)

    }
}