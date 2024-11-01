package com.easybuilder.common.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.io.Serializable

/**
 * MExt
 * Created by sky.Ren on 2024/11/1.
 * Description: 自定义扩展函数
 */
/**
 * 打印日志
 */
fun String.log(tag: String?="ILogger") {
    Log.d(tag, this)
}

/**
 * 获取SharedPreferences
 */
val Context.preferences: SharedPreferences
    get() = getSharedPreferences("config", Context.MODE_PRIVATE)

/**
 * 保存数据-> Boolean
 */
fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}

/**
 * 获取数据-> Boolean
 */
fun SharedPreferences.getBoolean(key: String, defaultValue: Boolean = false): Boolean {
    return getBoolean(key, defaultValue)
}

/**
 * 保存数据-> String
 */
fun SharedPreferences.putString(key: String, value: String) {
    edit().putString(key, value).apply()
}

/**
 * 获取数据-> String
 */
fun SharedPreferences.getString(key: String, defaultValue: String = ""): String {
    return getString(key, defaultValue) ?: defaultValue
}

/**
 * 保存数据-> Int
 */
fun SharedPreferences.putInt(key: String, value: Int) {
    edit().putInt(key, value).apply()
}

/**
 * 获取数据-> Int
 */
fun SharedPreferences.getInt(key: String, defaultValue: Int = 0): Int {
    return getInt(key, defaultValue)
}

/**
 * 保存数据-> Long
 */
fun SharedPreferences.putLong(key: String, value: Long) {
    edit().putLong(key, value).apply()
}

/**
 * 获取数据-> Long
 */
fun SharedPreferences.getLong(key: String, defaultValue: Long = 0L): Long {
    return getLong(key, defaultValue)
}

/**
 * 保存数据-> Float
 */
fun SharedPreferences.putFloat(key: String, value: Float) {
    edit().putFloat(key, value).apply()
}

/**
 * 获取数据-> Float
 */
fun SharedPreferences.getFloat(key: String, defaultValue: Float = 0f): Float {
    return getFloat(key, defaultValue)
}

/**
 * 保存数据-> Set
 */
fun SharedPreferences.putStringSet(key: String, value: Set<String>) {
    edit().putStringSet(key, value).apply()
}

/**
 * 获取数据-> Set
 */
fun SharedPreferences.getStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> {
    return getStringSet(key, defaultValue) ?: defaultValue
}

/**
 * 清除数据
 */
fun SharedPreferences.clear() {
    edit().clear().apply()
}

/**
 * 启动Activity
 */
inline fun <reified T : Any> Context.startActivity(bundles: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundles?.let {
        intent.putExtras(bundles)
    }
    startActivity(intent)
}

/**
 * 启动Activity
 */
inline fun <reified T : Any> Context.startActivity(vararg params: Pair<String, Any?>) {
    val intent = Intent(this, T::class.java)
    params.forEach {
        val key = it.first
        val value = it.second
        when (value) {
            is Int -> intent.putExtra(key, value)
            is Long -> intent.putExtra(key, value)
            is CharSequence -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
            is Float -> intent.putExtra(key, value)
            is Double -> intent.putExtra(key, value)
            is Char -> intent.putExtra(key, value)
            is Short -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is Serializable -> intent.putExtra(key, value)
            is Bundle -> intent.putExtra(key, value)
            is Parcelable -> intent.putExtra(key, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(key, value)
                value.isArrayOf<String>() -> intent.putExtra(key, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(key, value)
            }
            is IntArray -> intent.putExtra(key, value)
            is LongArray -> intent.putExtra(key, value)
            is FloatArray -> intent.putExtra(key, value)
            is DoubleArray -> intent.putExtra(key, value)
            is CharArray -> intent.putExtra(key, value)
            is ShortArray -> intent.putExtra(key, value)
            is BooleanArray -> intent.putExtra(key, value)
            else -> throw IllegalArgumentException("Unsupported bundle component")
        }
    }
    startActivity(intent)
}

/**
 * 启动Activity For Result
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    launcher: ActivityResultLauncher<Intent>,
    vararg params: Pair<String, Any?>
) {
    val intent = Intent(this, T::class.java)
    params.forEach {
        val key = it.first
        val value = it.second
        when (value) {
            is Int -> intent.putExtra(key, value)
            is Long -> intent.putExtra(key, value)
            is CharSequence -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
            is Float -> intent.putExtra(key, value)
            is Double -> intent.putExtra(key, value)
            is Char -> intent.putExtra(key, value)
            is Short -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is Serializable -> intent.putExtra(key, value)
            is Bundle -> intent.putExtra(key, value)
            is Parcelable -> intent.putExtra(key, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(key, value)
                value.isArrayOf<String>() -> intent.putExtra(key, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(key, value)
            }
            is IntArray -> intent.putExtra(key, value)
            is LongArray -> intent.putExtra(key, value)
            is FloatArray -> intent.putExtra(key, value)
            is DoubleArray -> intent.putExtra(key, value)
            is CharArray -> intent.putExtra(key, value)
            is ShortArray -> intent.putExtra(key, value)
            is BooleanArray -> intent.putExtra(key, value)
            else -> throw IllegalArgumentException("Unsupported bundle component")
        }
    }
    launcher.launch(intent)
}

/**
 * 请求权限
 */
fun AppCompatActivity.requestPermission(
    permission: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val launcher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                onGranted()
            } else {
                onDenied()
            }
        }

    if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        launcher.launch(permission)
    }
}

/**
 * 请求权限-批量
 */
fun Fragment.requestPermission(
    permission: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit
) {
    val launcher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                onGranted()
            } else {
                onDenied()
            }
        }

    if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        launcher.launch(permission)
    }
}

fun AppCompatActivity.requestPermissions(
    permissions: Array<String>,
    onGranted: () -> Unit,
    onDenied: (deniedPermissions: List<String>) -> Unit
) {
    val launcher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedPermissions = result.filterValues { !it }.keys
            if (deniedPermissions.isEmpty()) {
                onGranted()
            } else {
                onDenied(deniedPermissions.toList())
            }
        }

    val deniedPermissions = permissions.filter {
        ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
    }

    if (deniedPermissions.isEmpty()) {
        onGranted()
    } else {
        launcher.launch(deniedPermissions.toTypedArray())
    }
}

fun Fragment.requestPermissions(
    permissions: Array<String>,
    onGranted: () -> Unit,
    onDenied: (deniedPermissions: List<String>) -> Unit
) {
    val launcher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val deniedPermissions = result.filterValues { !it }.keys
            if (deniedPermissions.isEmpty()) {
                onGranted()
            } else {
                onDenied(deniedPermissions.toList())
            }
        }

    val deniedPermissions = permissions.filter {
        ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
    }

    if (deniedPermissions.isEmpty()) {
        onGranted()
    } else {
        launcher.launch(deniedPermissions.toTypedArray())
    }
}
