package com.easybuilder.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.easybuilder.common.utils.PermissionHelper
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * BaseVMFragment
 * Created by sky.Ren on 2024/9/4.
 * Description: Fragment基类
 */
abstract class BaseVMFragment<VB:ViewBinding,VM:ViewModel>(
    private val viewModelClass: Class<VM>
    ,
    //是否绑定Activity使用同一个viewmodel实例
    private val isBindActivity:Boolean=false
):Fragment() {
    //ViewModel
    protected lateinit var mBinding:VB
    //ViewModel
    protected lateinit var mViewModel:VM
    //权限工具
    protected val permissionTool:PermissionHelper? by lazy {
        activity?.let {
            PermissionHelper(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = createViewModel(isBindActivity,viewModelClass)
        mBinding = initViewBinding()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize();
    }

    private fun initialize() {
        observeViewModel()
        initView()
        loadData()
    }

    /**
     * 订阅ViewModel
     */
    private fun observeViewModel(){
        lifecycleScope.launch {
            observe()
        }
    }

    protected suspend abstract fun observe()

    /**
     * 初始化View
     */
    protected abstract fun initView()

    /**
     * 加载数据
     */
    protected abstract fun loadData()

    /**
     * 处理返回事件
     */
    protected fun handleBackPressed():Boolean{
        return false
    }

    /**
     * 初始化ViewBinding
     */
    @Suppress("UNCHECKED_CAST")
    private fun initViewBinding(): VB {
        val superClass = javaClass.genericSuperclass
        val bindingClass = (superClass as ParameterizedType).actualTypeArguments[0] as Class<VB>
        val inflateMethod: Method = bindingClass.getMethod("inflate", LayoutInflater::class.java)
        return inflateMethod.invoke(null, layoutInflater) as VB
    }

    /**
     * 创建viewmodleprovoider
     */
    private fun <T:ViewModel> createViewModel(bindActivity:Boolean=false, cls:Class<T>) : T{
        when(bindActivity){
            true -> return ViewModelProvider(requireActivity())[cls]
            false -> return ViewModelProvider(this)[cls]
        }
    }
}