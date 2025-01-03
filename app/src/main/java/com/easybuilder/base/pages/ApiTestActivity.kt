package com.easybuilder.base.pages

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.easybuilder.base.MainViewModel
import com.easybuilder.base.R
import com.easybuilder.base.adapter.TestKotlinAdapter
import com.easybuilder.base.databinding.ActivityApiTestBinding
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.threadpool.ThreadTask
import com.easybuilder.common.threadpool.ThreadUtil
import com.easybuilder.common.utils.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class ApiTestActivity : BaseVMActivity<ActivityApiTestBinding,MainViewModel>(
    viewModelClass = MainViewModel::class.java
) {
    private var testKotlinAdapter: TestKotlinAdapter? = null

    override suspend fun observe() {
    }

    override fun initView() {

        ThreadUtil.getInstance().addTask(object : ThreadTask() {
            override fun runTask() {
                "${Thread.currentThread().id}".log("===")
            }
        });

        Glide.with(this).load("https://img0.baidu.com/it/u=1008951549,1654888911&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=800").into(mBinding.ivHead)
    }

    override fun loadData() {
        mBinding.rv.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        testKotlinAdapter = TestKotlinAdapter(this)
        mBinding.rv.adapter = testKotlinAdapter

        testKotlinAdapter?.setDataList(mutableListOf("1","2","3"))

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

//    override fun onBackPressed() {
//        super.onBackPressed()
//        "测试".log()
//    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        "keydonw".log()
        return super.onKeyDown(keyCode, event)
    }
}