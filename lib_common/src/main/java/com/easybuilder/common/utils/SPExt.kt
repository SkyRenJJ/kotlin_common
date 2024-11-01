package com.easybuilder.common.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * MExt
 * Created by sky.Ren on 2024/11/1.
 * Description: 自定义扩展函数
 */
fun String.log(tag: String?="ILogger") {
    Log.d(tag, this)
}

val Context.preferences: SharedPreferences
    get() = getSharedPreferences("config", Context.MODE_PRIVATE)


fun SharedPreferences.putBoolean(key: String, value: Boolean) {
    edit().putBoolean(key, value).apply()
}

fun SharedPreferences.getBoolean(key: String, defaultValue: Boolean = false): Boolean {
    return getBoolean(key, defaultValue)
}


fun SharedPreferences.putString(key: String, value: String) {
    edit().putString(key, value).apply()
}

fun SharedPreferences.getString(key: String, defaultValue: String = ""): String {
    return getString(key, defaultValue) ?: defaultValue
}

fun SharedPreferences.putInt(key: String, value: Int) {
    edit().putInt(key, value).apply()
}

fun SharedPreferences.getInt(key: String, defaultValue: Int = 0): Int {
    return getInt(key, defaultValue)
}

fun SharedPreferences.putLong(key: String, value: Long) {
    edit().putLong(key, value).apply()
}

fun SharedPreferences.getLong(key: String, defaultValue: Long = 0L): Long {
    return getLong(key, defaultValue)
}

fun SharedPreferences.putFloat(key: String, value: Float) {
    edit().putFloat(key, value).apply()
}

fun SharedPreferences.getFloat(key: String, defaultValue: Float = 0f): Float {
    return getFloat(key, defaultValue)
}

fun SharedPreferences.putStringSet(key: String, value: Set<String>) {
    edit().putStringSet(key, value).apply()
}

fun SharedPreferences.getStringSet(key: String, defaultValue: Set<String> = emptySet()): Set<String> {
    return getStringSet(key, defaultValue) ?: defaultValue
}

fun SharedPreferences.clear() {
    edit().clear().apply()
}
