package com.example.bilitv.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.ui.PlayerView
import com.example.bilitv.view.model.VideoPlayerScreenModel

@Composable
fun VideoPlayerScreen(aid: String?, bvid: String?) {
    val viewModel: VideoPlayerScreenModel = hiltViewModel()
    val playInfo = viewModel.playInfo.collectAsState()
    LaunchedEffect(Unit) {
        if (playInfo.value == null) {
            viewModel.requestPlayInfo(aid = aid, bvid = bvid)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = viewModel.exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxSize()
    )
}

