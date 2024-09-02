package com.example.bilitv.view.model

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.okhttp.OkHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MergingMediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.data.VideoUrlResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import javax.inject.Inject

@HiltViewModel
class VideoPlayerScreenModel @Inject constructor (
    @ApplicationContext context: Context,
    okHttpClient: OkHttpClient,
    private val bilibiliApi: BilibiliApi
): ViewModel() {
    val exoPlayer = ExoPlayer.Builder(context).build().apply {
        this.playWhenReady = true
        this.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> Log.d("ExoPlayer", "Player is ready.")
                    Player.STATE_BUFFERING -> Log.d("ExoPlayer", "Player is buffering.")
                    Player.STATE_ENDED -> Log.d("ExoPlayer", "Playback ended.")
                    Player.STATE_IDLE -> Log.d("ExoPlayer", "Player is idle.")
                }
            }
            override fun onPlayerError(error: PlaybackException) {
                Log.e("ExoPlayer", "Player error: ${error.message}")
            }
        })
    }

    private val exoPlayerDataSourceFactory = OkHttpDataSource.Factory { okHttpClient.newCall(it) }

    private var _playInfo = MutableStateFlow<VideoUrlResponse?>(value = null)
    val playInfo = _playInfo.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        exoPlayer.stop()
        exoPlayer.release()
    }

    @OptIn(UnstableApi::class)
    fun requestPlayInfo(aid: String?, bvid: String?) {
        viewModelScope.launch {
            var cid: String? = null
            cid = if (aid != null) {
                async { bilibiliApi.getCidByAid(aid).data?.first()?.cid }.await()
            } else if (bvid != null) {
                async { bilibiliApi.getCidByBvid(bvid).data?.first()?.cid }.await()
            } else {
                return@launch
            }
            cid?.let {
                val playData = bilibiliApi.getPlayUrl(aid, bvid, cid.toLong()).data
                _playInfo.value = playData
                playData?.let { info ->
                    val factory = ProgressiveMediaSource.Factory(exoPlayerDataSourceFactory)
                    val urlList = mutableListOf(info.dash?.video?.first()?.baseUrl ?: "")
                    info.dash?.audio?.first()?.baseUrl?.let { urlList.add(it) }
                    val sources =
                        urlList.map {
                            factory.createMediaSource(MediaItem.fromUri(it))
                        }.toTypedArray()
                    urlList.forEach {
                        Log.d("exo", "url $it")
                    }
                    exoPlayer.setMediaSource(MergingMediaSource(*sources))
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true
                }
            }
        }
    }
}