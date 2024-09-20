package com.easybuilder.common.threadpool

import android.text.TextUtils
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicLong

/**
 * BaseThreadFactory
 * Created by sky.Ren on 2024/9/20.
 * Description: 线程工厂
 */

class BaseThreadFactory(builder: Builder) : ThreadFactory {
    companion object {
        private val AVAILABLE_PROCESSORS: Int = Runtime.getRuntime().availableProcessors()
        private val defaultCorePoolSize: Int =
            2.coerceAtLeast((AVAILABLE_PROCESSORS - 1).coerceAtMost(4))
        private val defaultMaxPoolSize = ((AVAILABLE_PROCESSORS * 2) + 1)
    }

    private val orderTag: AtomicLong = AtomicLong(0)

    var threadFactory: ThreadFactory? = null
    var exceptionHandler: Thread.UncaughtExceptionHandler? = null
    var threadTag: String? = null
    var priority: Int? = 0
    var isDaemon: Boolean = false
    var corePoolSize: Int = 0
    var maxPoolSize: Int = 0
    var aliveTime: Long = 0
    var queue: BlockingQueue<Runnable>? = null

    init {
        if (builder.defaultThreadFactory == null) {
            this.threadFactory = Executors.defaultThreadFactory()
        } else {
            this.threadFactory = builder.defaultThreadFactory
        }
        this.corePoolSize = builder.corePoolSize
        this.maxPoolSize = defaultMaxPoolSize
        if (this.maxPoolSize!! < this.corePoolSize!!) {
            throw IllegalArgumentException("corePoolSize > maxPoolSize")
        }
        this.aliveTime = builder.aliveTime
        if (builder.defaultQueue == null) {
            this.queue = LinkedBlockingQueue(256)
        } else {
            this.queue = builder.defaultQueue
        }
        if (TextUtils.isEmpty(builder.defaultTag)) {
            this.threadTag = "BaseThreadFactory"
        } else {
            this.threadTag = builder.defaultTag
        }
        this.priority = builder.defaultPriority
        this.isDaemon = builder.defaultDaemon
        this.exceptionHandler = builder.defaultHandler
        this.orderTag.set(0)

    }


    override fun newThread(r: Runnable?): Thread {
        val newThread = threadFactory?.newThread(r)
        threadTag?.let {
            newThread?.name = "${it}-${orderTag.incrementAndGet()}"
        }
        exceptionHandler?.let {
            newThread?.uncaughtExceptionHandler = it
        }
        priority?.let {
            newThread?.priority = it
        }
        isDaemon?.let {
            newThread?.isDaemon = it
        }
        return newThread!!
    }

    class Builder {
        var defaultThreadFactory: ThreadFactory? = null

        var defaultHandler: Thread.UncaughtExceptionHandler? = null

        var defaultTag: String? = null

        var defaultPriority: Int? = null
        var defaultDaemon: Boolean = false
        var corePoolSize: Int = defaultCorePoolSize

        private var totalPoolSize: Int =
            defaultMaxPoolSize
        var aliveTime: Long = 30

        var defaultQueue: BlockingQueue<Runnable>? = null

        fun setNamingPattern(str: String?): Builder {
            if (str == null) {
                throw NullPointerException("Naming pattern must not be null!")
            }
            this.defaultTag = str
            return this
        }

        fun setDaemon(): Builder {
            this.defaultDaemon = java.lang.Boolean.TRUE
            return this
        }

        fun setCorePoolSize(): Builder {
            this.corePoolSize = 1
            return this
        }

        fun setTotalPoolSize(i: Int): Builder {
            if (this.corePoolSize <= 0) {
                throw NullPointerException("corePoolSize  must > 0!")
            }
            this.totalPoolSize = i
            return this
        }

        fun setThreadQueue(blockingQueue: BlockingQueue<Runnable>?): Builder {
            this.defaultQueue = blockingQueue
            return this
        }

        private fun reset() {
            this.defaultThreadFactory = null
            this.defaultHandler = null
            this.defaultTag = null
            this.defaultPriority = null
            this.defaultDaemon = false
        }

        fun build(): BaseThreadFactory {
            val factory: BaseThreadFactory =
                BaseThreadFactory(this)
            reset()
            return factory
        }
    }
}