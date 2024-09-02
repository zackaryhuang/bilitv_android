package com.example.bilitv.view.model

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.data.VideoDetailResponse
import com.jing.bilibilitv.http.data.VideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailScreenModel @Inject constructor(
    private val bilibiliApi: BilibiliApi,
) : ViewModel() {

    private val _videoDetailInfo = MutableStateFlow<VideoDetailResponse?>(value = null)
    val videoDetailInfo = _videoDetailInfo.asStateFlow()

    fun requestVideoDetail(aid: String?, bvid: String?) {
        viewModelScope.launch {
            bilibiliApi.getVideoDetail(bvid = bvid, aid = aid).data?.let { res ->
                _videoDetailInfo.value = res
            }
        }
    }
}