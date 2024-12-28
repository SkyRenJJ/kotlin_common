package com.easybuilder.base.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.easybuilder.base.databinding.ItemMessage1Binding
import com.easybuilder.common.base.adapter.BaseRecyclerViewAdapterKt
import com.easybuilder.common.base.adapter.BaseViewHolder
import com.easybuilder.common.utils.log

/**
 * TestKotlinAdapter
 * Created by sky.Ren on 2024/12/28.
 * Description:
 */
class TestKotlinAdapter(context: Context) :
    BaseRecyclerViewAdapterKt<String, ItemMessage1Binding>(context) {
    override fun bindData(
        holder: BaseViewHolder<ItemMessage1Binding>?,
        data: String,
        position: Int
    ) {
        data.log("======")
        holder?.binding?.mssage?.text = data
    }

    override fun bindMultiTypeData(holder: RecyclerView.ViewHolder?, t: String, position: Int) {
    }
}