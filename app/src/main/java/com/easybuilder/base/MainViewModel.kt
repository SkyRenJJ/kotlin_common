package com.easybuilder.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.easybuilder.common.net.sample.SampleRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    val sampleRepo: SampleRepository by lazy {
        SampleRepository()
    }

    val textFlow:MutableSharedFlow<String> = MutableSharedFlow()

    fun test(block:(String)->Unit) {
        viewModelScope.launch {
            sampleRepo.test {
                block(it)
            }
        }
    }
}