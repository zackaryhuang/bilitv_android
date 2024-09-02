package com.example.bilitv.view

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ClickableSurfaceColors
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.bilitv.R
import java.nio.LongBuffer

@Preview
@Composable
fun ProfileScreen() {
    val scrollState = rememberScrollState()
    Surface(selected = false, onClick = { /*TODO*/ }) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ProfileInfoCard(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight()
                )
                ProfileItem(title = "我的关注", paintResource = R.drawable.icon_follow_2, modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight())
                ProfileItem(title = "设置", paintResource = R.drawable.icon_setting, modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight())
                ProfileItem(title = "关于", paintResource = R.drawable.icon_about, modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight())
                ProfileItem(title = "退出", paintResource = R.drawable.icon_exit, modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight())
            }
        }
    }
}
@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    paintResource: Int
) {
    Surface(
        colors = ClickableSurfaceDefaults.colors(containerColor = Color(0xFFFF6699) , focusedContainerColor = Color.White.copy(0.3F)),
        onClick = { /*TODO*/ }
    ) {
        Column(
            modifier
                .fillMaxSize()
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp),
                painter = painterResource(id = paintResource),
                contentDescription = ""
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun ProfileInfoCard(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter),
            colors = ClickableSurfaceDefaults.colors(containerColor = Color(0xFFFF6699) , focusedContainerColor = Color.White.copy(0.3F)),
            onClick = {

            }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight(0.9F)
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 50.dp)
            ) {
                Text(text = "Mr黄黄黄黄黄先生", fontSize = 18.sp)

                Spacer(modifier = Modifier.height(5.dp))
                
                Text(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Red)
                        .height(20.dp)
                        .width(80.dp),
                    text = "年度大会员", fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "硬币: 1351   B币：0",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                LevelBar(currentLevel = 5, currentValue = 16773, nextLevelNeedValue = 12027)
            }
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            UserAvatar(
                cover = "https://i0.hdslb.com/bfs/face/59da329a536b148b0e8d19143b686d2da06dad93.jpg",
            )
        }
    }

}

@Composable
fun LevelBar(currentLevel: Long, currentValue: Long, nextLevelNeedValue: Long) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(painter = painterResource(id = R.drawable.icon_level_5), contentDescription = "", modifier = Modifier.weight(0.1F))
            Spacer(modifier = Modifier.width(5.dp))
            ProgressBar(currentValue = currentValue, totalValue = currentValue + nextLevelNeedValue, modifier = Modifier.weight(0.8F))
            Spacer(modifier = Modifier.width(5.dp))
            Image(painter = painterResource(id = R.drawable.icon_level_6), contentDescription = "", modifier = Modifier.weight(0.1F))
        }

        Text(
            text = "当前成长${currentLevel}，距离升级到 Lv.${currentLevel + 1}还需要${nextLevelNeedValue}",
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontSize = 10.sp
        )
    }
}

@Composable
fun ProgressBar(currentValue: Long, totalValue: Long, modifier: Modifier) {
    Box(
        modifier
            .height(4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.Gray)
    ) {
        Box(
            modifier
                .background(Color.Yellow)
                .fillMaxHeight()
                .fillMaxWidth(currentValue.toFloat() / totalValue.toFloat())
        ) {

        }
    }
}

@Composable
fun UserAvatar(cover: String, modifier: Modifier = Modifier) {
    Box{
        RemoteImage(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .fillMaxSize(),
            url = cover
        )

        Image(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .width(20.dp)
                .height(20.dp),
            painter = painterResource(id = R.drawable.icon_vip),
            contentDescription = ""
        )
    }
}