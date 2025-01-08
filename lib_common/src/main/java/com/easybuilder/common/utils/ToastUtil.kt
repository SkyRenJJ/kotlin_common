package com.easybuilder.common.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

/**
 * ToastUtil
 * Created by sky.Ren on 2025/1/8.
 * Description:
 */
object ToastUtil {
    private var bindContentId: Int = 0
    private var sToast: Toast? = null
    private var sToastLayout: Toast? = null

    /**
     * 显示Toast
     */
    fun showReplace(context: Context, text: String) {
        if (sToast == null) {
            sToast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        } else {
            sToast!!.setText(text)
        }
        sToast!!.show()
    }

    /**
     * 自定义Toast布局
     */
    fun bindView(context: Context, view: View, contentId: Int) {
        this.bindContentId = contentId;
        sToastLayout = Toast(context)
        sToastLayout!!.view = view
        sToastLayout!!.duration = Toast.LENGTH_SHORT
    }

    /**
     * 显示自定义Toast
     */
    fun _showReplace(text: String) {
        if (sToastLayout == null) {
            throw RuntimeException("must be called the method of 'bindView' before _showReplace!!")
        } else {
            sToastLayout!!.view!!.findViewById<TextView>(bindContentId).text = text
            sToastLayout!!.show()
        }
    }

}