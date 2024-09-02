package com.example.bilitv.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.data.DisplayableData
import com.jing.bilibilitv.http.data.DynamicItem
import com.jing.bilibilitv.http.data.VideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowScreenModel @Inject constructor(
    private val bilibiliApi: BilibiliApi,
) : ViewModel() {

    var page = 1
    var offset = ""

    private val _feedItems = MutableStateFlow<List<DynamicItem>>(value = mutableListOf<DynamicItem>())
    val feedItems = _feedItems.asStateFlow()

    fun requestFeed() {
        viewModelScope.launch {
            bilibiliApi.getDynamicList(page, offset).data?.let { res ->
                _feedItems.value = res.items
                page += 1
                offset = res.offset
            }
        }
    }

    fun requestMoreFeed() {
        viewModelScope.launch {
            bilibiliApi.getDynamicList(page, offset).data?.let { res ->
                _feedItems.value += res.items
                offset = res.offset
            }
        }
    }
}