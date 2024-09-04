package com.easybuilder.common.base.launcher

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment

/**
 * BaseLauncher
 * Created by sky.Ren on 2024/9/4.
 * Description: ActivityResultContract封装
 */
abstract class BaseLauncher<I, O>(
    private val contract: ActivityResultContract<I, O>
) : ActivityResultCallback<O> {
    lateinit var launcher: ActivityResultLauncher<I>
    lateinit var activity: Activity

    fun registerForActivityResult(owner: ComponentActivity) {
        activity = owner
        launcher = owner.registerForActivityResult(contract, this)
    }

    fun registerForActivityResult(owner: Fragment) {
        activity = owner.requireActivity()
        launcher = owner.registerForActivityResult(contract, this)
    }

    override fun onActivityResult(result: O) {
    }

}