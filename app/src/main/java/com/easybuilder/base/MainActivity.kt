package com.easybuilder.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybuilder.base.adapter.PageAdapter
import com.easybuilder.base.bean.PageBean
import com.easybuilder.base.databinding.ActivityMainBinding
import com.easybuilder.base.location.MyLocationManager
import com.easybuilder.base.pages.ApiTestActivity
import com.easybuilder.base.pages.BaseRvAdapterKtActivity
import com.easybuilder.base.pages.ChatActivity
import com.easybuilder.base.pages.ComposeActivity
import com.easybuilder.base.pages.PagingLibraryActivity
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.net.INetCallback
import com.easybuilder.common.net.sample.TestBean
import com.easybuilder.common.net.sample.Value
import com.easybuilder.common.utils.PermissionHelper
import com.easybuilder.common.utils.log
import com.easybuilder.common.utils.preferences
import com.easybuilder.common.utils.startActivityByClsExt
import com.easybuilder.common.utils.startActivityExt
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.UnknownHostException

class MainActivity : BaseVMActivity<ActivityMainBinding, MainViewModel>(
    MainViewModel::class.java
) {
    private val pageAdapter:PageAdapter by lazy { PageAdapter(this) }
    override suspend fun observe() {
    }

    override fun initView() {

//        ImmersionBar.with(this)
//            .statusBarColorTransformEnable(true)
//            .keyboardEnable(true)
//            .statusBarDarkFont(false, 0.2f)
//            .init()


        mBinding.rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rv.adapter = pageAdapter
        pageAdapter.setOnItemClickListener { _, data ->
           val pageData = data as  PageBean
            startActivityByClsExt(pageData.cls,null)
        }
       var pages = ArrayList<PageBean>()

        pages.add(PageBean(ChatActivity::class.java, "流式聊天-倒叙",R.mipmap.ic_launcher))
        pages.add(PageBean(ApiTestActivity::class.java, "Api测试-协程",R.mipmap.ic_launcher))
        pages.add(PageBean(ComposeActivity::class.java, "Compose-UI",R.mipmap.ic_launcher))
        pages.add(PageBean(PagingLibraryActivity::class.java, "Paging-UI",R.mipmap.ic_launcher))
        pages.add(PageBean(BaseRvAdapterKtActivity::class.java, "kotlin版本适配器测试",R.mipmap.ic_launcher))

        pageAdapter.dataList = pages

    }



    private fun onClick(view: View?) {
        mViewModel.test3()
    }

    override fun loadData() {
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