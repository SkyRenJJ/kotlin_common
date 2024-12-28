package com.easybuilder.base.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.easybuilder.base.R

/**
 * TmcbarView
 * Created by sky.Ren on 2024/11/26.
 * Description:
 */
class TmcbarView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var congestionColor: Int = Color.RED // 默认拥堵色
    private var nonCongestionColor: Int = Color.GREEN // 默认非拥堵色
    private var progress: Float = 0f // 进度条值（0.0 - 1.0）
    private var carIcon: Drawable? = null // 进度标志（如车标）
    private var carIconWidth: Float = 0f // 车标宽度

    init {
        // 获取自定义属性（如果有）
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TmcbarView,
            0, 0
        ).apply {
            try {
                congestionColor = getColor(R.styleable.TmcbarView_congestionColor, Color.RED)
                nonCongestionColor = getColor(R.styleable.TmcbarView_nonCongestionColor, Color.GREEN)
                progress = getFloat(R.styleable.TmcbarView_progress, 0f)
                carIcon = getDrawable(R.styleable.TmcbarView_carIcon)
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // 确保车标宽度不为 0，动态计算宽度
        carIcon?.let {
            carIconWidth = it.intrinsicWidth.toFloat()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 计算当前状态的颜色
        val barColor = if (progress < 0.5f) nonCongestionColor else congestionColor

        // 计算进度条宽度，确保宽度与车标一致
        val barWidth = carIconWidth.coerceAtMost(width.toFloat()) // 确保宽度不超过视图的宽度

        // 绘制进度条背景
        val paint = Paint().apply {
            color = Color.LTGRAY
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, barWidth, height.toFloat(), paint)

        // 绘制进度条
        val progressPaint = Paint().apply {
            color = barColor
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, height * (1 - progress), barWidth, height.toFloat(), progressPaint)

        // 绘制进度车标（如果有）
        carIcon?.let {
            val iconHeight = it.intrinsicHeight.toFloat()
            val iconX = (barWidth - carIconWidth) / 2 // 水平居中
            val iconY = height * (1 - progress) - iconHeight / 2
            it.setBounds(iconX.toInt(), iconY.toInt(), (iconX + carIconWidth).toInt(), (iconY + iconHeight).toInt())
            it.draw(canvas)
        }
    }

    // 用于动态更新进度值
    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate() // 重新绘制
    }

    // 用于动态更新拥堵状态颜色
    fun setCongestionColor(color: Int) {
        this.congestionColor = color
        invalidate()
    }

    // 用于动态更新非拥堵状态颜色
    fun setNonCongestionColor(color: Int) {
        this.nonCongestionColor = color
        invalidate()
    }

    // 用于动态更新车标图标
    fun setCarIcon(icon: Drawable) {
        this.carIcon = icon
        carIconWidth = icon.intrinsicWidth.toFloat() // 更新车标宽度
        invalidate()
    }
}

