package com.easybuilder.common.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.easybuilder.common.base.launcher.ActivityLauncher
import com.easybuilder.common.utils.PermissionHelper
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
/**
 * BaseVMActivity
 * Created by sky.Ren on 2024/9/4.
 * Description: Activity基类
 */
abstract class BaseVMActivity<VB :ViewBinding,VM:ViewModel>(
    private var viewModelClass: Class<VM>
):AppCompatActivity() {
    //ViewBinding的实例
    protected lateinit var mViewModel:VM
    //ViewBinding的实例
    protected lateinit var mBinding:VB
//    //Activity启动器
//    protected val activityLauncher by lazy { ActivityLauncher() }

    //权限工具
//    protected lateinit var permissionTool: PermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        activityLauncher.registerForActivityResult(this)
//        if (!::permissionTool.isInitialized) permissionTool = PermissionHelper(this)
        // 初始化ViewModel
        mViewModel = createViewModel(viewModelClass)
        // 初始化ViewBinding
        mBinding = initViewBinding()
        // 绑定View
        setContentView(mBinding.root)
        // 初始化订阅
        observeViewModel()
        // 初始化View
        initView()
        // 加载数据
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
    private fun <T:ViewModel> createViewModel(cls:Class<T>) : T{
        return ViewModelProvider(this)[cls]
    }

    companion object{
        val TAG:String = BaseVMActivity::class.java.simpleName
    }

}