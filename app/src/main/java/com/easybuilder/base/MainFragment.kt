package com.easybuilder.base

import com.easybuilder.base.databinding.FragmentMainBinding
import com.easybuilder.common.base.BaseVMFragment

/**
 * MainFragment
 * Created by sky.Ren on 2024/9/4.
 * Description: 主页
 */
class MainFragment:BaseVMFragment<FragmentMainBinding,MainViewModel>(
    MainViewModel::class.java,
    false
) {

   companion object{
       fun newInstance()=MainFragment()
   }

    override suspend fun observe() {
        mViewModel.textFlow.collect {
            mBinding.tv2.text = it
        }
    }

    override fun initView() {
    }

    override fun loadData() {
    }
}