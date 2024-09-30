package com.easybuilder.common.net.sample

import com.easybuilder.common.net.INetCallback
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
    suspend fun test(): Response<TestBean<Value>>

    @GET("/FRwK4ja2fc753bd4e3704d2b368b5bbe0104e39e0835f1c/bn/api/1.0/auth/oauth/token?branchID=0")
    fun test2(): Call<TestBean<Value>>
}

//https://mockapi.eolink.com/FRwK4ja2fc753bd4e3704d2b368b5bbe0104e39e0835f1c/bn/api/1.0/auth/oauth/token?branchID=0
class SampleRepository{
    val client: ApiService by lazy {
        RetrofitClient.createService<ApiService>("https://mockapi.eolink.com/")
    }
    //方式1
    suspend fun test(): Response<TestBean<Value>>? {
        return client.test()
    }
    //方式2
    suspend fun test2(onSuccess:(data:Response<TestBean<Value>>?)->Unit={}, onError:(e:Exception)->Unit={}): Unit{
        try {
            onSuccess(client.test())
        }catch (e:Exception){
            onError(e)
        }
    }
    //方式3
    fun test3(): Call<TestBean<Value>> {
        return client.test2()
    }

   suspend fun test4(callback:INetCallback<Response<TestBean<Value>>>): Unit {
        try {
            callback.onSuccess(client.test())
        }catch (e:Exception){
            callback.onFailed(e)
        }
    }
}