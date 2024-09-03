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
import com.example.bilitv.view.model.LivePlayerScreenModel

@Composable
fun LivePlayerScreen(roomID: String) {
    val viewModel: LivePlayerScreenModel = hiltViewModel()
    LaunchedEffect(Unit) {
        if (viewModel.allPlayInfos.isEmpty()) {
            viewModel.requestStream(roomID)
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