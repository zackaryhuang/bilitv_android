package com.example.bilitv.view

import android.support.v4.media.session.MediaControllerCompat.PlaybackInfo
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
fun VideoPlayerScreen(playData: PlayData) {
    val viewModel: VideoPlayerScreenModel = hiltViewModel()
    val playInfo = viewModel.playInfo.collectAsState()
    LaunchedEffect(Unit) {
        if (playInfo.value == null) {
            viewModel.requestPlayInfo(playData)
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

data class PlayData(
    var aid: String? = null,
    var bvid: String? = null,
    var cid: String? = null,
    var seasonID: String? = null,
    var episodeID: String? = null,
) {
    fun isPGC(): Boolean {
        return this.episodeID?.isNotEmpty() == true || this.seasonID?.isNotEmpty() == true
    }
}

