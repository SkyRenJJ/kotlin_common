package com.easybuilder.base
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.easybuilder.base.databinding.ActivityMainBinding
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.net.RetrofitClient
import kotlinx.coroutines.launch
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
            mViewModel.test {
                lifecycleScope.launch {
                    mViewModel.textFlow.emit("${System.currentTimeMillis()} $it" )
                }
            }
    }

    override fun loadData() {
        mBinding.tv.text = "测试"


    }

}