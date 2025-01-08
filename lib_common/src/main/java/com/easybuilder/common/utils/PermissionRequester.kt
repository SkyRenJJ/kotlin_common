package com.easybuilder.common.utils

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

/**
 * PermissionRequester
 * Created by sky.Ren on 2025/1/8.
 * Description: 权限请求
 */
class PermissionRequester(
    private val activity: ComponentActivity,
    private val onPermissionGranted: (List<String>) -> Unit,
    private val onPermissionDenied: (List<String>) -> Unit
) {

    private val permissionLauncher: ActivityResultLauncher<Array<String>> =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val grantedPermissions = result.filter { it.value }.map { it.key }
            val deniedPermissions = result.filter { !it.value }.map { it.key }

            if (grantedPermissions.isNotEmpty()) {
                onPermissionGranted(grantedPermissions)
            }

            if (deniedPermissions.isNotEmpty()) {
                onPermissionDenied(deniedPermissions)
            }
        }

    // 请求单个权限
    fun requestPermission(permission: String) {
        permissionLauncher.launch(arrayOf(permission))
    }

    // 请求多个权限
    fun requestPermissions(permissions: List<String>) {
        permissionLauncher.launch(permissions.toTypedArray())
    }

    // 检查权限是否已被授予
    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
    }

    // 检查多个权限是否已被授予
    fun arePermissionsGranted(permissions: List<String>): Boolean {
        return permissions.all { isPermissionGranted(it) }
    }
}