package com.example.bilitv.view.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.api.PassportApi
import com.jing.bilibilitv.http.data.VideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val bilibiliApi: BilibiliApi,
) : ViewModel() {

    private val _feedItems = MutableStateFlow<List<VideoInfo>>(value = mutableListOf<VideoInfo>())
    val feedItems = _feedItems.asStateFlow()

    fun requestFeed() {
        viewModelScope.launch {
            bilibiliApi.getRecommendation().data?.items?.let {
                _feedItems.value = it
            }
        }
    }

    fun requestMoreFeed() {
        viewModelScope.launch {
            bilibiliApi.getRecommendation().data?.items?.let {
                _feedItems.value += it
            }
        }
    }
}