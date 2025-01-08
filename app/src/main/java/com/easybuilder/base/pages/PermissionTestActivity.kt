package com.easybuilder.base.pages

import android.Manifest
import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import android.webkit.PermissionRequest
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.easybuilder.base.R
import com.easybuilder.common.utils.PermissionRequester
import com.easybuilder.common.utils.ToastUtil
import com.easybuilder.common.utils.log
import com.easybuilder.common.utils.logw
import com.easybuilder.common.utils.logwtf
import java.util.Arrays

/**
 * PermissionTestActivity
 * Created by sky.Ren on 2025/1/8.
 * Description:
 */
class PermissionTestActivity : ComponentActivity() {
    val permissonRequest =
        PermissionRequester(
            this,
            onPermissionDenied = {
                // 权限被拒绝时执行的操作
                for (i in 0..it.size - 1) {
                    it[i]?.let { permission ->
                        // 处理单个权限被拒绝的情况
                        "权限被拒绝: $permission".log("Permission")
                    }
                }
            },
            onPermissionGranted = {
                // 权限被拒绝时执行的操作
                for (i in 0..it.size - 1) {
                    it[i]?.let { permission ->
                        // 处理单个权限被拒绝的情况
                        "权限被同意: $permission".log("Permission")
                    }
                }
            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClickItem(this@PermissionTestActivity)
        }

        ToastUtil.bindView(this,LayoutInflater.from(this).inflate(R.layout.item_message_1,null),R.id.mssage)
    }


    @Composable
    fun ClickItem(activity: ComponentActivity) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "测试多个权限", modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        permissonRequest.requestPermissions(
                            Arrays.asList(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                        )
                    }
                    .padding(10.dp))
                Text(text = "测试单个权限", modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        permissonRequest.requestPermissions(
                            Arrays.asList(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            )
                        )
                    }
                    .padding(10.dp)
                )
                Text(text = "测试权限状态", modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                       var result =  permissonRequest.arePermissionsGranted(
                            Arrays.asList(
                                Manifest.permission.CAMERA
                            )
                        )
                        ToastUtil._showReplace("结果：---${result}_${System.currentTimeMillis()}")
                    }
                    .padding(10.dp)
                )
            }
        }
    }
}