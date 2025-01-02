package com.easybuilder.base.pages

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataAdapter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybuilder.base.MainViewModel
import com.easybuilder.base.R
import com.easybuilder.base.databinding.ActivityPagingBinding
import com.easybuilder.base.databinding.ItemPageBinding
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.base.adapter.BaseRecyclerViewAdapterKt
import com.easybuilder.common.base.adapter.BaseRecylerViewAdapter.BaseViewHolder
import com.easybuilder.common.utils.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * PagingLibraryActivity
 * Created by sky.Ren on 2025/1/2.
 * Description: 分页库测试
 */

class ItemAdapter : PagingDataAdapter<String, ItemAdapter.ItemViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            itemView.findViewById<TextView>(R.id.title).text = item
        }
    }
}

//分页配置
class ItemRepository(){
    fun getItems(): Pager<Int, String> {
        return Pager(
            config = PagingConfig(pageSize = 1, enablePlaceholders = true),
            pagingSourceFactory = { DemoPageingSource() }
        )
    }
}

// 分页数据源
class DemoPageingSource:PagingSource<Int,String>(){
    override fun getRefreshKey(state: PagingState<Int, String>): Int? {
        return state.anchorPosition?.let {
            anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        try {
            var currentPage = params.key?:1

            var startIndex = if (currentPage == 3) 1 else 10
            var endIndex = 20
            delay(500)
            val data = (startIndex..endIndex).map {
                "第${currentPage}页，数据$it"
            }.toList()

            "开始加载${currentPage}, size=${params.loadSize}".log("pager")
            // 计算前一页和下一页的键
            val prevKey = if (currentPage == 1) null else currentPage - 1
            val nextKey = currentPage + 1

            return LoadResult.Page(data, prevKey, nextKey)
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}

class PagingViewModel : ViewModel() {
   var itemRepository = ItemRepository()
    val pagingData = itemRepository.getItems().flow.cachedIn(viewModelScope)
}

class PagingLibraryActivity : BaseVMActivity<ActivityPagingBinding, PagingViewModel>(
    PagingViewModel::class.java
) {
    private lateinit var adapter: ItemAdapter

    override suspend fun observe() {

    }

    override fun initView() {
        adapter = ItemAdapter()
        mBinding.rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mBinding.rv.adapter = adapter

    }

    override fun loadData() {
        lifecycleScope.launch {
            mViewModel.pagingData.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}