package com.easybuilder.common.net

/**
 * INetCallback
 * Created by sky.Ren on 2024/9/30.
 * Description: 网络回调
 */
interface INetCallback<T> {
    fun onSuccess(t: T)
    fun onFailed(e:Exception)
}