package com.easybuilder.base.pages

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybuilder.base.MainViewModel
import com.easybuilder.base.R
import com.easybuilder.base.adapter.MessageAdapter
import com.easybuilder.base.bean.MessageBean
import com.easybuilder.base.databinding.ActivityChatBinding
import com.easybuilder.common.base.BaseVMActivity
import com.easybuilder.common.utils.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatActivity : BaseVMActivity<ActivityChatBinding,MainViewModel>(
    viewModelClass = MainViewModel::class.java,
), View.OnClickListener {
    var messageAdapter = MessageAdapter(this)
    var linearMgr = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
    var handler = Handler()
    var isDragging = false

    override suspend fun observe() {
    }

    override fun initView() {
        linearMgr.stackFromEnd = true
        mBinding.chatList.layoutManager = linearMgr
        mBinding.chatList.adapter = messageAdapter

        mBinding.btAdd.setOnClickListener(this)
        mBinding.btGenerate.setOnClickListener(this)
        mBinding.chatList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val findLastCompletelyVisibleItemPosition =
                    linearMgr.findFirstCompletelyVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    isDragging = true
                }else{
                    if (findLastCompletelyVisibleItemPosition ==0){
                        isDragging = false
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                "onScrolled".log()

            }

        })

    }

    override fun loadData() {
        var messageList = listOf(
            MessageBean("cscscscscs",1),
//            MessageBean("cscscscscs",2),
//            MessageBean("cscscscscs",1),
//            MessageBean("cscscscscs",2),
//            MessageBean("cscscscscs",1),
//            MessageBean("cscscscscs",2),
        )
        messageAdapter.addDataList(messageList)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_add -> {
                messageAdapter.addData(0,MessageBean("cscscscscs",2))
            }
            R.id.bt_generate -> {
                var text = "这个错误信息表明你的 Activity 需要使用 Theme.AppCompat 或其子类主题，但当前的主题不符合这个要求。这通常发生在你尝试使用 AppCompatActivity 或其他需要 AppCompat 主题的组件时，但你的应用主题不是基于 Theme.AppCompat 的。\n" +
                        "\n" +
                        "解决方法\n" +
                        "方法一：确保你的应用主题是 AppCompat 主题\n" +
                        "打开 res/values/styles.xml 文件。\n" +
                        "确保你的应用主题继承自 Theme.AppCompat 或其子类。\n" +
                        "例如：\n" +
                        "\n" +
                        "xml\n" +
                        "复制代码\n"
                val singleItem = messageAdapter.dataList.get(0)

                lifecycleScope.launch {
                    for (i in 0..text.length-1){
                        singleItem.msg+=text[i]
                        delay(100)
                        messageAdapter.notifyDataSetChanged()
                        if (!isDragging){
                            linearMgr.scrollToPositionWithOffset(0,0)
                        }
                    }
                }
            }
        }
    }
}