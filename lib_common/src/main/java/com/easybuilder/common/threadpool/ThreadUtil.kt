package com.easybuilder.common.threadpool

/**
 * ThreadUtil
 * Created by sky.Ren on 2024/9/20.
 * Description: 线程池工具类
 */
class ThreadUtil() {
    private var mThreadPool: ThreadPool? = null

    init {
        mThreadPool = ThreadPool.getInstance("default_threadpool")
    }


    companion object {
        @Volatile
        var _instance: ThreadUtil? = null

        fun getInstance(): ThreadUtil =  _instance ?: synchronized(this) {
            _instance ?: ThreadUtil().also { _instance = it }
        }
    }

    fun destroy() {
        try {
            if (_instance != null) {
                _instance!!.mThreadPool?.destory()
            }

            _instance?.mThreadPool = null
            _instance = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addTask(task: ThreadTask) {
        mThreadPool?.addTask(task)
    }

    fun cancelTask(task: ThreadTask) {
        task.cancelTask()
    }
}