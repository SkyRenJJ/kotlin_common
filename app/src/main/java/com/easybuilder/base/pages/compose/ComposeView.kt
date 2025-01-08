package com.easybuilder.base.pages.compose

import android.graphics.Paint.Align
import androidx.collection.mutableFloatListOf
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.easybuilder.base.R
import com.easybuilder.common.utils.log
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * ComposeView
 * Created by sky.Ren on 2024/12/30.
 * Description:
 */
@Composable
fun LargeText(string: String? = null, isLarge: Boolean = false) {
    var large by remember {
        mutableStateOf(isLarge)
    }
    var content by remember {
        mutableStateOf(string)
    }
    var fontSize by remember {
        mutableStateOf(if (large) 20.sp else 50.sp)
    }
    val interactionSource = remember { MutableInteractionSource() }

    Text(
        text = content ?: "",
        modifier = Modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .clickable(
                indication = null,  // 设置蓝色波纹效果
                interactionSource = interactionSource, enabled = true, onClickLabel = "超市菜市场"
            ) {
                large = !large
                content = "点击了--${System.currentTimeMillis()}"
                fontSize = if (large) 20.sp else 50.sp
            },
        style = TextStyle(
            fontSize = fontSize,
            textDirection = TextDirection.Rtl,
            color = Color.Blue,
            background = Color.Transparent
        ),
    )
}

@Composable
fun TMCView() {
    var progress by remember {
        mutableIntStateOf(50)
    }
    var height by remember {
        mutableIntStateOf(300)
    }

    val bottomOffset by animateDpAsState(
        targetValue = -progress.dp,
        animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
    )

    Box(
        modifier = Modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .height(height.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color.Green)
    ) {
        Image(
            painter = painterResource(R.drawable.icon_location),
            contentDescription = "车标",
            modifier = Modifier
                .width(25.dp)
                .height(25.dp)
                .align(Alignment.BottomCenter)
//                .offset(y = (if (progress < height) progress.dp else height.dp - 25.dp) * -1)
                .offset(y = bottomOffset)
        )
    }

    LaunchedEffect(Unit) {
        while (progress < height) {
            progress += 1
            if (progress >= height) {
                progress = 0
            }
            delay(100)
        }
    }
}

@Composable
fun AnimatedSizeChange() {
    var isClicked by remember { mutableStateOf(false) }

    // 使用 animateDpAsState 动画化宽度和高度的变化
    val size by animateDpAsState(
        targetValue = if (isClicked) 200.dp else 100.dp,
        animationSpec = tween(durationMillis = 1000) // 动画时长为 1000 毫秒
    )

    Box(
        modifier = Modifier
            .size(size) // 动画化尺寸
            .background(Color.Blue)
    ) {
        Button(
            onClick = { isClicked = !isClicked },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Toggle Size")
        }
    }
}

data class DemoData(
    val id: Int,
    val title: String,
)

/**
 * 使用material3自带的SwipeToDismissBox，滑动后放手松开立即执行
 * Box里面嵌套两层Row，当上面一层Row被滑动移走时，下面那层Row就会展示出来，两层Row布局都是全部充满Box的。
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeToDismissBoxDemo(list: MutableList<DemoData>) {
    val data = remember {
        mutableStateListOf<DemoData>()
    }
    data.addAll(list)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
    ) {
        //items务必添加key，否则会造成显示错乱
        itemsIndexed(data, key = { index, item -> item.id }) { index, item ->
            //index和item都是最原始的数据，一旦onDelete和onChange过，index和item就都不准了，因此根据item的id作为唯一标识查找
            SwipeToDismiss(
                modifier = Modifier.animateItemPlacement(), //添加移除时的动画
                content = { Text(item.title) },
                onDelete = { data.remove(data.find { it.id == item.id }) },
                onChange = {
                    data[data.indexOf(data.find { it.id == item.id })] =
                        item.copy(title = "Item has change: ${item.id}")
                }
            )
        }
    }
}

//使用material3自带的SwipeToDismissBox，滑动后放手松开立即执行
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismiss(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
    onDelete: () -> Unit,
    onChange: () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) { //滑动后放手会执行
                onDelete()
                return@rememberSwipeToDismissBoxState true
            }
            if (it == SwipeToDismissBoxValue.StartToEnd) { //滑动后放手会执行
                onChange()
            }
            return@rememberSwipeToDismissBoxState false
        }, positionalThreshold = { //滑动到什么位置会改变状态，滑动阈值
            it / 4
        })
    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(50.dp),
        backgroundContent = {
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.StartToEnd -> Color.Green
                    SwipeToDismissBoxValue.EndToStart -> Color.Red
                    else -> Color.LightGray
                }, label = ""
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color),
                contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) Alignment.CenterStart else Alignment.CenterEnd
            ) {
                if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "",
                        modifier = Modifier
                    )
                else
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "",
                        modifier = Modifier
                    )
            }
        },
        content = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center,
                content = content
            )
        })
}

@Composable
fun ViewPage(data: MutableList<DemoData>) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { data.size })
    VerticalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        modifier = Modifier.fillMaxSize()
    ) { page: Int ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Blue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = data.get(page).title,
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.Cyan)
                    .align(Alignment.Center),
                fontSize = 30.sp,
                style = TextStyle(background = Color.Red),
                textAlign = TextAlign.Center
            )
        }

    }
}

//滚动实现卫星效果
@Composable
fun satellitView(main: String?, childs: MutableList<String>?) {
    var offsetX = remember {
        mutableStateListOf(1.0f,-1.0f,0.0f)
    }
    var offsetY = remember {
        mutableStateListOf(0.0f,0.0f,-1.0f)
    }
    var isOpen by remember {
        mutableStateOf(false)
    }


    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        for (i in 0 until childs!!.size) {
            val animateDpAsState = animateDpAsState(targetValue = if(isOpen) 50.dp * offsetX.get(i) else 0.dp)
            val animateDpAsStateY = animateDpAsState(targetValue = if(isOpen) 50.dp * offsetY.get(i) else 0.dp)
            Text(
                text = childs[i],
                modifier = Modifier
                    .offset(x = animateDpAsState.value, y = animateDpAsStateY.value)
                    .height(50.dp)
                    .width(50.dp)
                    .clickable {
                        isOpen = !isOpen
                    },
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                style = TextStyle(background = Color.Red),

            )
        }
    }
}


@Composable
fun MyCanvas() {
    Canvas(modifier = Modifier.size(200.dp)) {
        // 绘制一个圆形
        drawCircle(color = Color.Blue, radius = 50f, center = center)

        // 绘制一个矩形
        drawRect(
            color = Color.Red,
            size = androidx.compose.ui.geometry.Size(100f, 100f),
            topLeft = androidx.compose.ui.geometry.Offset(50f, 50f)
        )

        // 绘制一个路径
        var path = Path()
        drawPath(
            path = path.apply {
                moveTo(10f, 10f)
                lineTo(100f, 10f)
                lineTo(50f, 100f)
                close()
            },
            color = Color.Green
        )
    }
}
