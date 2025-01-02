package com.easybuilder.base.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.animation.ValueAnimator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.easybuilder.base.R
import com.easybuilder.base.pages.compose.AnimatedSizeChange
import com.easybuilder.base.pages.compose.DemoData
import com.easybuilder.base.pages.compose.LargeText
import com.easybuilder.base.pages.compose.SwipeToDismissBoxDemo
import com.easybuilder.base.pages.compose.TMCView
import com.easybuilder.base.pages.compose.ViewPage
import kotlinx.coroutines.delay
import java.util.Timer
import java.util.TimerTask

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var data = mutableListOf<DemoData>()
        repeat(10){
            data.add(DemoData(it,"item $it"))
        }
        setContent {
//            Greeting("hello compose")
//            LargeText("hello compose")
//            TMCView()
//            AnimatedSizeChange()
//            SwipeableExample()
//            SwipeToDismissBoxDemo(data)
            ViewPage(data)
        }
    }

}

@Composable
fun ListItem(name: String) {
    val colors = listOf(Color.White, Color.Red, Color.Green, Color.Blue)
    var colorIndex by remember { mutableStateOf(0) }
    var backgroundColor by remember { mutableStateOf(Color.White) }

    Text(text = "${name}",
        style = TextStyle(
            fontSize = 30.sp,
            color = colors[colorIndex],
            background = backgroundColor
        ),
        modifier = Modifier.clickable {
//            colorIndex = (colorIndex + 1) % colors.size
        })


    LaunchedEffect(Unit) {
//        while (true) {
//            delay(1000)
//            colorIndex = (colorIndex + 1) % colors.size
//        }

        val animator = android.animation.ValueAnimator.ofArgb(
            Color.White.toArgb(),
            Color.Red.toArgb(),
            Color.Green.toArgb(),
            Color.Blue.toArgb()
        ).apply {
            duration = 3000 // 3 seconds
            repeatCount = android.animation.ValueAnimator.INFINITE
            repeatMode = android.animation.ValueAnimator.REVERSE

            addUpdateListener { animation ->
                backgroundColor = Color(animation.animatedValue as Int)
            }
        }
        animator.start()
        // Ensure to stop the animator when the composable leaves the composition
    }
}


@Composable
fun Greeting(name: String) {
    Row() {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.drawable.test),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp, 100.dp)
                .width(200.dp)
                .height(200.dp)
                .clip(
                    RoundedCornerShape(50.dp)
                )
        )

        LazyColumn {
            items(
                listOf(
                    1
                )
            ) {
                ListItem(name = "$name $it")
            }
        }
    }


}