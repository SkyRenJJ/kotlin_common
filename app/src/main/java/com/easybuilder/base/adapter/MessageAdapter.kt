package com.easybuilder.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.easybuilder.base.bean.MessageBean
import com.easybuilder.base.databinding.ItemMessage1Binding
import com.easybuilder.base.databinding.ItemMessage2Binding
import com.easybuilder.common.base.adapter.BaseRecylerViewAdapter

/**
 * MessageAdapter
 * Created by sky.Ren on 2024/11/6.
 * Description: 消息适配器
 */
class MessageAdapter(context: Context) : BaseRecylerViewAdapter<MessageBean, ViewBinding>(context) {
    override fun bindData(holder: BaseViewHolder<ViewBinding>?, data: MessageBean?, position: Int) {
    }

    override fun bindMultiTypeData(
        holder: RecyclerView.ViewHolder?,
        t: MessageBean?,
        position: Int
    ) {
        when (t?.userType) {
            1 -> {
                var typeHolder = holder as MessageType1ViewHolder
                typeHolder.binding.mssage.text = "${position}：\n${t.msg}"
            }


            2 -> {
                var typeHolder = holder as MessageType2ViewHolder
                typeHolder.binding.mssage.text = "${position}：--\n${t.msg}"

            }
        }
    }

    override fun onCreateMultiTypeVieiwHolder(
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> {
               return MessageType1ViewHolder(ItemMessage1Binding.inflate(LayoutInflater.from(context), parent, false))
            }

            2 -> {
                return MessageType2ViewHolder(ItemMessage2Binding.inflate(LayoutInflater.from(context), parent, false))
            }
        }
        return super.onCreateMultiTypeVieiwHolder(parent, viewType)
    }

    override fun getItemMultiType(position: Int): Int {
        return dataList?.get(position)?.userType!!
    }

    override fun isMultitype(): Boolean {
        return true
    }
}

class MessageType1ViewHolder(val binding: ItemMessage1Binding) : RecyclerView.ViewHolder(binding.root)
class MessageType2ViewHolder(val binding: ItemMessage2Binding) : RecyclerView.ViewHolder(binding.root)