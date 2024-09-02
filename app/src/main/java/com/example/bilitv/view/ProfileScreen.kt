package com.example.bilitv.view

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ClickableSurfaceColors
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.example.bilitv.R
import com.example.bilitv.view.model.FollowScreenModel
import com.example.bilitv.view.model.ProfileScreenModel
import com.jing.bilibilitv.http.data.DisplayableAndPlayableData
import com.jing.bilibilitv.http.data.DisplayableData
import com.jing.bilibilitv.http.data.PlayableData
import com.jing.bilibilitv.http.data.UserInfo
import kotlinx.coroutines.launch
import java.nio.LongBuffer

@Composable
fun ProfileScreen(
    onSelectVideo: (PlayableData) -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val viewModel: ProfileScreenModel = hiltViewModel()
    val recentVideos = viewModel.recentViewedItems.collectAsState()
    val watchLaterVideos = viewModel.watchLaterItems.collectAsState()
    val userInfo = viewModel.loginInfo.collectAsState()
    scrollState.viewportSize
    LaunchedEffect(Unit) {
        if (recentVideos.value.isEmpty()) {
            viewModel.requestRecent()
        }

        if (watchLaterVideos.value.isEmpty()) {
            viewModel.requestWatchLater()
        }

        if (userInfo.value == null) {
            viewModel.requestLoginInfo()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Bottom
        ) {
            userInfo.value?.let {
                ProfileInfoCard(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight(),
                    userInfo = it,
                    onFocusChanged = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(0)
                        }
                    }
                )
            }

            ProfileItem(
                title = "我的关注",
                paintResource = R.drawable.icon_follow_2,
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
            )
            ProfileItem(
                title = "设置",
                paintResource = R.drawable.icon_setting,
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
            )
            ProfileItem(
                title = "关于",
                paintResource = R.drawable.icon_about,
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
            )
            ProfileItem(
                title = "退出",
                paintResource = R.drawable.icon_exit,
                modifier = Modifier
                    .width(110.dp)
                    .fillMaxHeight()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        ProfileHorizontalVideoView(title = "最近播放", videos = recentVideos.value, onSelectVideo = {
            onSelectVideo(it)
        })
        ProfileHorizontalVideoView(title = "稍后观看", videos = watchLaterVideos.value, onSelectVideo = {
            onSelectVideo(it)
        })
    }
}

@Composable
fun ProfileHorizontalVideoView(title: String, videos: List<DisplayableAndPlayableData>, onSelectVideo: (PlayableData) -> Unit) {
    Column(
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight(600),
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            contentPadding = PaddingValues(5.dp)
        ) {
            item {
                videos.forEach { video ->
                    BorderedFocusableItem(
                        modifier = Modifier.width(200.dp),
                        onClick = {
                            onSelectVideo(video)
                        }
                    ) {
                        DisplayableVideoItem(
                            item = video,
                            onClick = { item ->
                                println(item.title)
                            }
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    title: String,
    paintResource: Int
) {
    Surface(
        colors = ClickableSurfaceDefaults.colors(containerColor = Color.White.copy(0.16F) , focusedContainerColor = Color.biliBlue),
        onClick = { /*TODO*/ }
    ) {
        Column(
            modifier
                .height(170.dp)
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier
                    .height(60.dp)
                    .width(60.dp),
                painter = painterResource(id = paintResource),
                contentDescription = ""
            )

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
    userInfo: UserInfo,
    onFocusChanged: (Unit) -> Unit
)
{
    Box(
        modifier
            .fillMaxSize()
            .height(200.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(0.9F)
        ) {
            UserAvatar(
                cover = userInfo.face,
                isVip = userInfo.vipType != 0
            )
        }
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onFocusChanged {
                    onFocusChanged(Unit)
                },
            colors = ClickableSurfaceDefaults.colors(containerColor = Color.White.copy(0.16F) , focusedContainerColor = Color.biliBlue),
            onClick = {

            }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 50.dp)
            ) {
                Text(text = userInfo.uname, fontSize = 18.sp)

                Spacer(modifier = Modifier.height(5.dp))
                if (userInfo.vipType >= 2) {
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
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "硬币: ${userInfo.money}   B币：${userInfo.wallet.bCoin}",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                LevelBar(currentLevel = userInfo.levelInfo.currentLevel, currentValue = userInfo.levelInfo.currentExp, nextLevelNeedValue = userInfo.levelInfo.nextExp - userInfo.levelInfo.currentExp)
            }
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
            text = "当前成长${currentValue}，距离升级到 Lv.${currentLevel + 1}还需要${nextLevelNeedValue}",
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
fun UserAvatar(cover: String, isVip: Boolean, modifier: Modifier = Modifier) {
    Box{
        RemoteImage(
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .clip(RoundedCornerShape(30.dp))
                .fillMaxSize(),
            url = cover
        )

        if (isVip) {
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
}

val Color.Companion.biliBlue: Color
    get() = Color(0xFF00AEEC)

val Color.Companion.biliPink: Color
    get() = Color(0xFFFF6699)
