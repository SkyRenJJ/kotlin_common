package com.easybuilder.base.adapter

import android.app.Activity
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.easybuilder.base.bean.PageBean
import com.easybuilder.base.databinding.ItemPageBinding
import com.easybuilder.common.base.adapter.BaseRecylerViewAdapter

/**
 * PageAdapter
 * Created by sky.Ren on 2024/11/6.
 * Description: 页面适配器
 */
class PageAdapter(context: Context?) : BaseRecylerViewAdapter<PageBean, ItemPageBinding>(context) {
    override fun bindData(
        holder: BaseViewHolder<ItemPageBinding>?,
        data: PageBean?,
        position: Int
    ) {
        data?.run {
            holder?.binding?.title?.text = data.title
        }
    }

    override fun bindMultiTypeData(holder: RecyclerView.ViewHolder?, t: PageBean?, position: Int) {
        TODO("Not yet implemented")
    }
}