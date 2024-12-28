package com.easybuilder.common.base.adapter

/**
 * BaseRecyclerViewService
 * Created by sky.Ren on 2024/12/23.
 * Description: 适配器功能
 */
interface BaseRecyclerViewService<T> {
    /**
     * 设置数据
     */
    fun setDataList(data:MutableList<T>)

    /**
     * 添加数据
     */
    fun  addDataList(data: MutableList<T>)

    /**
     * 添加数据
     */
    fun  addData(data: T)

    /**
     * 设置数据
     */
    fun  setData(index:Int,data: T)

    /**
     * 清除数据
     */
    fun  clearData()

    /**
     * 获取数据
     */
    fun getData():MutableList<T>?

    /**
     * 更新数据
     */
    fun updateItem(data: T,position: Int)

    /**
     * 移除数据
     */
    fun removeItem(position: Int)
}