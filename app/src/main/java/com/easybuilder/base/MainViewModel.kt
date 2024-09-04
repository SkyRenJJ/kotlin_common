package com.easybuilder.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easybuilder.common.net.sample.SampleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel:ViewModel() {
    val sampleRepo: SampleRepository by lazy {
        SampleRepository()
    }

    val textFlow:MutableSharedFlow<String> = MutableSharedFlow()

    fun test() {
        viewModelScope.launch {
            Log.d("====", "test: 111")
           var test = withContext(Dispatchers.IO) {
               Log.d("====", "test: ${Thread.currentThread().name}")
               sampleRepo.test()
            }
            Log.d("====", "test: 222  ${Thread.currentThread().name}")

            withContext(Dispatchers.Main) {
                Log.d("====", "test: 2525252  ${Thread.currentThread().name}")
                textFlow.emit(test.message!!)
            }
            Log.d("====", "test: 3333  ${Thread.currentThread().name}")

        }
    }
}