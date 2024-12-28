package com.easybuilder.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easybuilder.common.net.sample.SampleRepository
import com.easybuilder.common.net.sample.TestBean
import com.easybuilder.common.net.sample.Value
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {
    val sampleRepo: SampleRepository by lazy {
        SampleRepository()
    }
    val textFlow: MutableSharedFlow<String> = MutableSharedFlow()

    var call: Call<TestBean<Value>>? = null

    /**
     * 接口网络测试
     */
    fun test3(): Call<TestBean<Value>> {
        val call = sampleRepo.test3()
        call.enqueue(object : retrofit2.Callback<TestBean<Value>> {
            override fun onResponse(p0: Call<TestBean<Value>>, p1: Response<TestBean<Value>>) {
                viewModelScope.launch {
                    textFlow.emit(p1.body()?.message!!)
                }

            }

            override fun onFailure(p0: Call<TestBean<Value>>, p1: Throwable) {
                viewModelScope.launch {
                    textFlow.emit(p1.message!!)
                }
            }
        })
        return call
    }

    fun cancel() {
        call = test3()
        call?.cancel()

    }
}