package com.easybuilder.base.pages

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.easybuilder.base.MainViewModel
import com.easybuilder.base.R
import com.easybuilder.base.databinding.ActivityApiTestBinding
import com.easybuilder.common.base.BaseVMActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ApiTestActivity : BaseVMActivity<ActivityApiTestBinding,MainViewModel>(
    viewModelClass = MainViewModel::class.java
) {

    override suspend fun observe() {
    }

    override fun initView() {
//        lifecycleScope.launch {
//            val result1 = withContext(Dispatchers.IO){
//                performNetworkRequest("1")
//            }
//            println("Result 1: $result1")
//            val result2 = withContext(Dispatchers.IO){
//                performNetworkRequest("2")
//            }
//            println("Result 2: $result2")
//
//            val result3 = withContext(Dispatchers.IO){
//                performNetworkRequest("3")
//            }
//            println("Result 3: $result3")
//
//            val result4 = withContext(Dispatchers.IO){
//                performNetworkRequest("4")
//            }
//            println("Result 4: $result4")
//        }
        lifecycleScope.launch {
            val result1 = async(Dispatchers.IO){
                performNetworkRequest("1")
                println("1-----------")
            }
            val result2 = async(Dispatchers.IO){
                performNetworkRequest("2")
                println("2-----------")
            }
            val result3 = async(Dispatchers.IO){
                performNetworkRequest("3")
                println("3-----------")
            }
            val result4 = async(Dispatchers.IO){
                performNetworkRequest("4")
                println("4-----------")
            }
            println("Result 1: ${result1.await()}")
            println("Result 2: ${result2.await()}")
            println("Result 3: ${result3.await()}")
            println("Result 4: ${result4.await()}")
        }
    }

    override fun loadData() {
    }

    // 模拟网络请求的函数
    suspend fun performNetworkRequest(request: String): String {
//        delay(1000) // 模拟网络延迟
//        if (request == "Request 2") {
//            throw IOException("Network error for $request")
//        }
//        return "$request - response"
        if (request.equals("1")) {
            delay(2000)
        }
        val test = mViewModel.sampleRepo.test()
        return "$request   ${ test!!.body()!!.message }"
    }
}