package com.example.bilitv.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bilitv.view.model.VideoDetailScreenModel

@Composable
fun VideoDetailScreen(aid: String? = null, bvid: String? = null, cid: String) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val viewModel: VideoDetailScreenModel = hiltViewModel()
    val videoDetail = viewModel.videoDetailInfo.collectAsState()

    LaunchedEffect(Unit) {
        if (viewModel.videoDetailInfo.value == null) {
            viewModel.requestVideoDetail(aid, bvid)
        }
    }

    CenterText(text = "VideoDetail ${aid} ${bvid} ${videoDetail.value?.view?.title}")
}