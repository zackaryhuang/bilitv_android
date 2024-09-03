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
import com.jing.bilibilitv.http.api.LiveApi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import javax.inject.Inject

@UnstableApi
@HiltViewModel
class LivePlayerScreenModel @Inject constructor(
    @ApplicationContext context: Context,
    okHttpClient: OkHttpClient,
    private val liveApi: LiveApi,
) : ViewModel() {

    val allPlayInfos = mutableListOf<LivePlayUrlInfo>()

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

    override fun onCleared() {
        super.onCleared()
        exoPlayer.stop()
        exoPlayer.release()
    }

    @OptIn(UnstableApi::class)
    fun requestStream(roomID: String) {
        viewModelScope.launch {
            val streams = async {  liveApi.getLiveStreams(roomID = roomID.toLong()).data?.playUrlInfo?.playUrl?.streams }.await()
            streams?.let {
                it.forEach { stream ->
                    stream.formats.forEach { format ->
                        val formatName = format.formatName
                        format.codecs.forEach { codec ->
                            val qn = codec.currentQn
                            val baseUrl = codec.baseUrl
                            codec.urlInfos.forEach { urlInfo ->
                                val host = urlInfo.host
                                val extra = urlInfo.extra
                                val url = "$host$baseUrl$extra"
                                val playInfo =
                                    LivePlayUrlInfo(format = formatName, url = url, qn = qn)
                                allPlayInfos.add(playInfo)
                            }
                        }
                    }
                }
            }
            allPlayInfos.sortWith(Comparator { o1, o2 ->
                if ((o1.qn ?: 0) > (o2.qn ?: 0)) 1 else 0
            })

            allPlayInfos.sortWith(Comparator { o1, o2 ->
                if (o1.format == "fmp4") 1 else 0
            })

            allPlayInfos.first().url?.let { playUrl ->
                val mediaItem = MediaItem.fromUri(playUrl)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                exoPlayer.playWhenReady
            }
        }
    }
}

data class LivePlayUrlInfo(
    val format: String?,
    val url: String?,
    val qn: Long?
)