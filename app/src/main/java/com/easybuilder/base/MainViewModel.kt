package com.easybuilder.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easybuilder.common.net.sample.SampleRepository
import com.easybuilder.common.net.sample.TestBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel:ViewModel() {
    val sampleRepo: SampleRepository by lazy {
        SampleRepository()
    }

    val textFlow:MutableSharedFlow<String> = MutableSharedFlow()

    /**
     * 接口网络测试
     */
    fun test() {
        viewModelScope.launch {
           var test :TestBean = withContext(Dispatchers.IO) {
               sampleRepo.test()
            }
            withContext(Dispatchers.Main) {
                textFlow.emit(test.message!!)
            }
        }
    }
}