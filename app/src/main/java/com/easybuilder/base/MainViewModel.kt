package com.easybuilder.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easybuilder.common.net.sample.SampleRepository
import com.easybuilder.common.net.sample.TestBean
import com.easybuilder.common.net.sample.Value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class MainViewModel:ViewModel() {
    val sampleRepo: SampleRepository by lazy {
        SampleRepository()
    }

    val textFlow:MutableSharedFlow<String> = MutableSharedFlow()

    /**
     * 接口网络测试
     */
    fun test() {
            sampleRepo.test2().enqueue(object : retrofit2.Callback<TestBean<Value>>{
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

//        viewModelScope.launch {
//           var test :TestBean<Value> = withContext(Dispatchers.IO) {
//               sampleRepo.test()
//            }
//            withContext(Dispatchers.Main) {
//                textFlow.emit(test.message!!)
//            }
//        }
    }
}