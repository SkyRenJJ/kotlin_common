package com.easybuilder.common.net.sample

/**
 * TestBean
 * Created by sky.Ren on 2024/9/4.
 * Description:
 */
data class TestBean<T>(
    val errCode: Int,
    val message: String,
    val status: Boolean,
    val value: T
)

data class Value(
    val refresh_token: Int,
    val token: String
)