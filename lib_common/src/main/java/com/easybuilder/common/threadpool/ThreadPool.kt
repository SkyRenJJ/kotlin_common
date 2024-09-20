package com.easybuilder.common.threadpool

import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * ThreadPool
 * Created by sky.Ren on 2024/9/20.
 * Description: 线程池
 */
class ThreadPool(threadFactory: BaseThreadFactory) :BaseThreadPool() {
    init {
        try {
            this.executor = ThreadPoolExecutor(threadFactory.corePoolSize, threadFactory.maxPoolSize,
                threadFactory.aliveTime, TimeUnit.SECONDS,
                threadFactory.queue, threadFactory)
            this.executor!!.allowCoreThreadTimeOut(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    companion object{

        fun getInstance(tag:String): ThreadPool {
            return ThreadPool(BaseThreadFactory.Builder().setNamingPattern(tag).build())

        }

        fun getInstance(threadFactory: BaseThreadFactory): ThreadPool {
            return ThreadPool(threadFactory)
        }
    }
}