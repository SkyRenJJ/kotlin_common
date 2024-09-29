package com.easybuilder.base
import android.Manifest
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.easybuilder.base.databinding.ActivityMainBinding
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.net.RetrofitClient
import com.easybuilder.common.threadpool.BaseThreadFactory
import com.easybuilder.common.threadpool.ThreadTask
import com.easybuilder.common.threadpool.ThreadUtil
import com.easybuilder.common.utils.PermissionHelper
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

class MainActivity : BaseVMActivity<ActivityMainBinding,MainViewModel>(
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
            .statusBarDarkFont(false,0.2f)
            .init()

        mBinding.tv.setOnClickListener(this::onClick)
//
//        val beginTransaction = supportFragmentManager.beginTransaction()
//        beginTransaction.replace(R.id.frag, MainFragment.newInstance())
//        beginTransaction.commit()
    }

    private fun onClick(view: View?) {
        mViewModel.test()
    }

    override fun loadData() {
        mBinding.tv.text = "测试"

//    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), {
//        if (it.resultCode == RESULT_OK) {
//            mBinding.tv.text = "测试2"
//        }
//    })
//
//        val drawable = resources.getDrawable(R.drawable.test)
//        val intrinsicWidth = drawable.intrinsicWidth
//        val intrinsicHeight = drawable.intrinsicHeight
//
//        Log.d(TAG, "loadData image=: "+intrinsicWidth+" "+intrinsicHeight )
//
//
//        val heightPixels = resources.displayMetrics.heightPixels
//        val widthPixels = resources.displayMetrics.widthPixels
//        Log.d(TAG, "loadData screen=: "+widthPixels+" "+heightPixels )
//
//
//        val layoutParams = mBinding.ivTest.layoutParams
////        layoutParams.height = intrinsicHeight
//        layoutParams.width = intrinsicWidth
//
////        mBinding.ivTest.layoutParams = layoutParams
//        mBinding.ivTest.setImageDrawable(drawable)

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
        lifecycleScope.launch(handler){
            try {
                mViewModel.sampleRepo.test().let {
                    if (it != null) {
                        if (it.isSuccessful) {
                            Log.d(TAG, "loadData: success")
                        }else{
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

}