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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response

class MainActivity : BaseVMActivity<ActivityMainBinding,MainViewModel>(
    MainViewModel::class.java
) {
    override suspend fun observe() {
        mViewModel.textFlow.collect {
            mBinding.tv.text = it
        }
    }

    override fun initView() {
        mBinding.tv.setOnClickListener(this::onClick)

        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.replace(R.id.frag, MainFragment.newInstance())
        beginTransaction.commit()
    }

    private fun onClick(view: View?) {
//            mViewModel.textFlow.emit("${System.currentTimeMillis()}")
        mViewModel.test()
//        ThreadUtil.getInstance().destroy()

//        val hasPermission = permissionTool.hasPermission(Manifest.permission.CAMERA)
//        Log.d(TAG, "onClick: "+hasPermission)
//        permissionTool.requestPermission(object : PermissionHelper.PermissionCallback {
//            override fun callback(
//                granted: MutableMap<String, Boolean>?,
//                denied: MutableMap<String, Boolean>?,
//                isAllGranted: Boolean,
//                isAllDenied: Boolean
//            ) {
//            }
//
//            override fun callback(result: Boolean?) {
//            }
//
//        },Manifest.permission.CAMERA)
    }

    override fun loadData() {
        mBinding.tv.text = "测试"

//    registerForActivityResult(ActivityResultContracts.StartActivityForResult(), {
//        if (it.resultCode == RESULT_OK) {
//            mBinding.tv.text = "测试2"
//        }
//    })

        ThreadUtil.getInstance().addTask(object: ThreadTask() {
            override fun runTask() {
                for (i in 0 until 1000) {
                    Thread.sleep(1000)
                    lifecycleScope.launch {
                        withContext(Dispatchers.Main) {
                                mBinding.tv.text = "测试3${i}"
                        }
                    }
                }

            }

        })
    }

}