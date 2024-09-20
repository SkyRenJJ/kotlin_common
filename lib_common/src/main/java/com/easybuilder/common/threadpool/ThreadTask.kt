package com.easybuilder.common.threadpool

/**
 * ThreadTask
 * Created by sky.Ren on 2024/9/20.
 * Description: 线程工作对象
 */
abstract class ThreadTask: Runnable {
    var callback: OnTaskListener? = null

    abstract fun runTask()

    fun cancelTask() {
        this.callback?.cancelTask(this)
    }

    override fun run() {
        try {
            if (!Thread.interrupted()){
                runTask()
                if (this.callback != null && !Thread.interrupted()){
                    this.callback?.runTask(this)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 任务监听
    interface OnTaskListener {
        // 开始执行任务
        fun runTask(task: ThreadTask)
        // 结束
        fun cancelTask(task: ThreadTask)
    }
}