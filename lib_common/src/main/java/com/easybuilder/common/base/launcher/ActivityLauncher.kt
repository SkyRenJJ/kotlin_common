package com.easybuilder.common.base.launcher

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

/**
 * ActivityLauncher
 * Created by sky.Ren on 2024/9/4.
 * Description: activity启动launcher
 */
class ActivityLauncher: BaseLauncher<Intent, ActivityResult>(
    ActivityResultContracts.StartActivityForResult()
) {
    var onActivityResult: (result: ActivityResult?) -> Unit = {  }

    inline fun <reified T : Activity> launch(
        crossinline setIntent: (intent: Intent) -> Unit = {},
        noinline onActivityResult: (result: ActivityResult?) -> Unit = {}
    ) {

        this.onActivityResult = onActivityResult
        val intent = Intent(activity, T::class.java)
        //根据配置设置 intent
        setIntent.invoke(intent)
        launcher.launch(intent)
    }


    fun launch(intent: Intent, onActivityResult: (result: ActivityResult?) -> Unit = {}) {
        this.onActivityResult = onActivityResult
        launcher.launch(intent)
    }

    override fun onActivityResult(result: ActivityResult) {
        super.onActivityResult(result)
        this.onActivityResult.invoke(result)
    }

}