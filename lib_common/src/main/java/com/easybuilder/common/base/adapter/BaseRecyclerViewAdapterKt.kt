package com.easybuilder.common.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.easybuilder.common.utils.log
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * BaseRecyclerViewAdapterKt
 * Created by sky.Ren on 2024/12/23.
 * Description: kotlin-recyclerview适配器
 */
abstract class BaseRecyclerViewAdapterKt<T, VB : ViewBinding>(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseRecyclerViewService<T> {
    companion object {
        const val TYPE_HEADER: Int = -1
        const val TYPE_FOOTER: Int = -2
    }

    private var dataList: MutableList<T>? = null
    private var headerView: Boolean = false
    private var footerView: Boolean = false
    private var isMultitype: Boolean = false
    var onItemClickListener: OnItemClickListener<T>? = null

    override fun setDataList(data: MutableList<T>) {
        dataList = data
        notifyDataSetChanged()
    }

    override fun addDataList(data: MutableList<T>) {
        dataList?.let {
            it.addAll(data)
            notifyDataSetChanged()
        }
    }

    override fun addData(data: T) {
        dataList?.let {
            it.add(data)
            notifyDataSetChanged()
        }
    }

    override fun setData(index: Int, data: T) {
        dataList?.let {
            it.set(index, data)
            notifyDataSetChanged()
        }
    }

    override fun clearData() {
        dataList?.clear()
    }

    override fun getData(): MutableList<T>? {
        return dataList
    }

    override fun updateItem(data: T, position: Int) {
        dataList?.let {
            it.set(position, data)
            notifyItemChanged(position)
        }
    }

    override fun removeItem(position: Int) {
        dataList?.let {
            it.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, getItemCount() - position);
        }
    }

    /**
     * 添加头部
     * 需要重写方法onCreateHeaderVieiwHolder
     *
     * @param flag
     */
    fun enableHeaderView(flag: Boolean) {
        if (headerView == flag) {
            return
        }
        headerView = flag
        notifyDataSetChanged()
    }

    /**
     * 添加底部
     * 需要重写方法onCreateFooterVieiwHolder
     *
     * @param flag
     */
    fun enableFooterView(flag: Boolean) {
        if (footerView == flag) {
            return
        }
        footerView = flag
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            return onCreateHeaderVieiwHolder(parent, viewType)!!
        } else if (viewType == TYPE_FOOTER) {
            return onCreateFooterVieiwHolder(parent, viewType)!!
        } else {
            if (isMultitype()) {
                return onCreateMultiTypeVieiwHolder(parent, viewType)!!
            } else {
                val type = javaClass.genericSuperclass
                if (type is ParameterizedType) {
                    val params = type.actualTypeArguments
                    val clazz = params[1] as Class<*>
                    var inflate: Method? = null
                    try {
                        inflate = clazz.getMethod(
                            "inflate",
                            LayoutInflater::class.java,
                            ViewGroup::class.java,
                            Boolean::class.javaPrimitiveType
                        )
                        val binding = inflate.invoke(
                            null, LayoutInflater.from(parent.context),
                            parent, false
                        ) as VB
                        return BaseViewHolder(binding)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        throw RuntimeException("Binding view not found.")
    }


    protected open fun onCreateHeaderVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        return null
    }

    protected open fun onCreateFooterVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        return null
    }

    protected open fun onCreateMultiTypeVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder? {
        return null
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        if (getItemViewType(pos) == TYPE_HEADER) {
            val tmpHoder = holder as HeaderViewHolder<*>
            bindDataForHeader(tmpHoder)
            return
        }
        if (getItemViewType(pos) == TYPE_FOOTER) {
            val tmpHoder = holder as FooterViewHolder<*>
            bindDataForFooter(tmpHoder)
            return
        }
        var position = pos
        if (headerView) {
            position = position - 1
        }
        if (dataList != null && !dataList!!.isEmpty()) {
            if (isMultitype()) {
                bindMultiTypeData(holder, dataList!![position], position)
            } else {
                val tmpHoder = holder as BaseViewHolder<VB>
                bindData(tmpHoder, dataList!![position], position)
                tmpHoder.binding.root.setOnClickListener {
                    this.onItemClickListener?.let {
                        it.onItemClick(position, dataList!![position])
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (headerView && position == 0) {
            return TYPE_HEADER
        }
        if (footerView && position == itemCount - 1) {
            return TYPE_FOOTER
        }
        if (isMultitype()) {
            return getItemMultiType(position)
        }
        return 0
    }

    protected open fun getItemMultiType(position: Int): Int {
        return 0
    }

    protected open fun isMultitype(): Boolean {
        return isMultitype
    }

    fun getContext(): Context {
        return context
    }

    override fun getItemId(position: Int): Long {
        return this.dataList?.get(position)?.hashCode()?.toLong()!!
    }

    override fun getItemCount(): Int {
        val i = if (dataList != null) dataList!!.size else 0
        return i + getHeaderCount() + getFooterCount()
    }

    fun getHeaderCount(): Int {
        return if (!headerView) 0 else 1
    }

    fun getFooterCount(): Int {
        return if (!footerView) 0 else 1
    }

    abstract fun bindData(holder: BaseViewHolder<VB>?, data: T, position: Int)

    abstract fun bindMultiTypeData(holder: RecyclerView.ViewHolder?, t: T, position: Int)

    protected open fun bindDataForHeader(holder: HeaderViewHolder<out ViewBinding?>?) {
    }

    protected open fun bindDataForFooter(holder: FooterViewHolder<out ViewBinding?>?) {
    }
}

class BaseViewHolder<VB : ViewBinding?>(val binding: VB) : RecyclerView.ViewHolder(
    binding!!.root
)

class HeaderViewHolder<HB : ViewBinding?>(val binding: HB) : RecyclerView.ViewHolder(
    binding!!.root
)

class FooterViewHolder<FB : ViewBinding?>(val binding: FB) : RecyclerView.ViewHolder(
    binding!!.root
)

/**
 * 点击事件相关
 *
 * @param <T>
</T> */
interface OnItemClickListener<T> {
    fun onItemClick(position: Int, data: T)
}

