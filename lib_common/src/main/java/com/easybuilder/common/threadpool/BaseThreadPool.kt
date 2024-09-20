package com.easybuilder.common.threadpool

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Future
import java.util.concurrent.ThreadPoolExecutor

/**
 * BaseThreadPool
 * Created by sky.Ren on 2024/9/20.
 * Description: 线程池抽象类
 */
abstract class BaseThreadPool {
    protected var executor: ThreadPoolExecutor? = null
    private var taskMap: ConcurrentHashMap<ThreadTask, Future<*>>? = ConcurrentHashMap()
    protected var taskListener: ThreadTask.OnTaskListener = object : ThreadTask.OnTaskListener {
        override fun runTask(task: ThreadTask) {
            removeQueue(task, false)
        }

        override fun cancelTask(task: ThreadTask) {
            removeQueue(task, true)
        }
    }

    /**
     * 是否包含任务
     */
    @Synchronized
    fun contain(task: ThreadTask): Boolean {
        return taskMap!!.containsKey(task)
    }

    /**
     * 添加任务
     */
    @Synchronized
    fun addQueue(task: ThreadTask, future: Future<*>) {
        if (!contain(task)) {
            taskMap!!.put(task, future)
        }
    }

    /**
     * 移除任务
     */
    @Synchronized
    fun removeQueue(task: ThreadTask, isCancel: Boolean) {
        if (contain(task)) {
            val remove = taskMap!!.remove(task)
            if (isCancel) {
                remove?.cancel(true)
            }
        }
    }

    /**
     * 添加任务
     */
    fun addTask(task: ThreadTask) {
        if (!contain(task) && this.executor != null && !this.executor!!.isShutdown) {
            task.callback = this.taskListener

            val submit = this.executor!!.submit(task)
            submit?.let {
                addQueue(task, submit)
            }
        }
    }

    /**
     * 关闭线程池
     */
    fun awaitTermination(timeout: Long, unit: java.util.concurrent.TimeUnit) {
        this.executor?.awaitTermination(timeout, unit)
    }

    /**
     * 销毁
     */
    fun destory() {
        try {
            for (task in taskMap!!.keys) {
                taskMap!!.get(task)?.cancel(true)
            }
            taskMap!!.clear()
            this.executor?.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}