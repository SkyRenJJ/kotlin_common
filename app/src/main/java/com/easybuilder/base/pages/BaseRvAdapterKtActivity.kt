package com.easybuilder.base.pages

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.easybuilder.base.databinding.ActivityPagingBinding
import com.easybuilder.base.databinding.ItemPageBinding
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.base.adapter.BaseRecyclerViewAdapterKt
import com.easybuilder.common.base.adapter.FooterViewHolder
import com.easybuilder.common.base.adapter.HeaderViewHolder
import com.easybuilder.common.base.adapter.OnItemClickListener
import com.easybuilder.common.utils.log
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * PagingLibraryActivity
 * Created by sky.Ren on 2025/1/2.
 * Description: 分页库测试
 */


class ItemCustomApdater(contxt: Context): BaseRecyclerViewAdapterKt<String, ItemPageBinding>(contxt) {
    override fun bindData(
        holder: com.easybuilder.common.base.adapter.BaseViewHolder<ItemPageBinding>?,
        data: String,
        position: Int
    ) {
        holder?.binding?.title?.text = data
    }

    override fun bindMultiTypeData(holder: RecyclerView.ViewHolder?, t: String, position: Int) {
        TODO("Not yet implemented")
    }

    override fun bindDataForHeader(holder: HeaderViewHolder<out ViewBinding?>?) {
        super.bindDataForHeader(holder)
        var h = holder as HeaderViewHolder<ItemPageBinding>
        h?.binding?.title?.text = "头部"
    }


    override fun bindDataForFooter(holder: FooterViewHolder<out ViewBinding?>?) {
        super.bindDataForFooter(holder)
        var h = holder as FooterViewHolder<ItemPageBinding>
        h?.binding?.title?.text = "底部"
    }

    override fun onCreateHeaderVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        val inflate = ItemPageBinding.inflate(LayoutInflater.from(getContext()))
        return HeaderViewHolder(inflate)
    }

    override fun onCreateFooterVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        val inflate = ItemPageBinding.inflate(LayoutInflater.from(getContext()))
        return FooterViewHolder(inflate)
    }
}

class BaseRvAdapterViewModel : ViewModel() {
}

class BaseRvAdapterKtActivity : BaseVMActivity<ActivityPagingBinding, BaseRvAdapterViewModel>(
    BaseRvAdapterViewModel::class.java
) {
    private lateinit var adapter: ItemCustomApdater

    override suspend fun observe() {

    }

    override fun initView() {
        adapter = ItemCustomApdater(this)
        adapter.enableFooterView(true)
        adapter.enableHeaderView(true)
        mBinding.rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mBinding.rv.adapter = adapter

        adapter.setDataList(mutableListOf("1", "2", "3", "4", "5"))
        adapter.onItemClickListener = object : OnItemClickListener<String> {
            override fun onItemClick(position: Int, data: String) {
                data.log("pager")
            }
        }

    }

    override fun loadData() {

    }
}