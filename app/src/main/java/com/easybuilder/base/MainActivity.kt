package com.easybuilder.base

import android.content.Context
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.easybuilder.base.databinding.ActivityMainBinding
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.net.INetCallback
import com.easybuilder.common.net.sample.TestBean
import com.easybuilder.common.net.sample.Value
import com.easybuilder.common.utils.preferences
import com.easybuilder.common.utils.putString
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.UnknownHostException

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>(
    MainViewModel::class.java
) {
    override suspend fun observe() {
        mViewModel.textFlow.collect {
            mBinding.tv.text = it
        }
    }

    override fun initView() {

        ImmersionBar.with(this)
            .statusBarColorTransformEnable(true)
            .keyboardEnable(true)
            .statusBarDarkFont(false, 0.2f)
            .init()

        mBinding.tv.setOnClickListener(this::onClick)

    }



    private fun onClick(view: View?) {
        mViewModel.test3()
    }

    override fun loadData() {
        mBinding.tv.text = "测试"
//        testNet2()
//        testNet3()
        testNet4()
    }


    //捕获异常的多种方式
    fun testNet1() {
        val handler = CoroutineExceptionHandler { _, exception ->
            if (exception is UnknownHostException) {
                // 处理 UnknownHostException
                Log.d(TAG, "loadData: error1")
            } else {
                // 处理其他异常
                Log.d(TAG, "loadData: error2")
            }
        }
        //方式1
        lifecycleScope.launch(handler) {
            try {
                mViewModel.sampleRepo.test().let {
                    if (it != null) {
                        if (it.isSuccessful) {
                            Log.d(TAG, "loadData: success")
                        } else {
                            Log.d(TAG, "loadData: error3")

                        }
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "loadData: error4")

            }
        }
        //方式2
//        lifecycleScope.launch{
//            try {
//                mViewModel.sampleRepo.test().let {
//                    if (it != null) {
//                        if (it.isSuccessful) {
//                            Log.d(TAG, "loadData: success")
//                        }else{
//                            Log.d(TAG, "loadData: error3")
//
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                Log.d(TAG, "loadData: error4")
//
//            }
//        }
    }

    //方式2
    fun testNet2() {
        lifecycleScope.launch {
            mViewModel.sampleRepo.test2(onSuccess = {
                Log.d(TAG, "loadData: success ")
            }, onError = {
                Log.d(TAG, "loadData: error5" + it.message)
            })
        }

    }

    //方式3
    fun testNet3() {
        mViewModel.test3()
    }

    //方式4
    fun testNet4() {
        lifecycleScope.launch {
            mViewModel.sampleRepo.test4(object : INetCallback<Response<TestBean<Value>>> {
                override fun onSuccess(t: Response<TestBean<Value>>) {
                    Log.d(TAG, "loadData: success4")
                }

                override fun onFailed(e: Exception) {
                    Log.d(TAG, "loadData: error4" + e.message)
                }

            })
        }
    }
}