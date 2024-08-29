package com.example.bilitv.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.data.DynamicItem
import com.jing.bilibilitv.http.data.HotVideInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HotScreenModel @Inject constructor(
    private val bilibiliApi: BilibiliApi,
) : ViewModel() {

    private var page = 1
    private var hasMore = true

    private val _feedItems = MutableStateFlow<List<HotVideInfo>>(value = mutableListOf<HotVideInfo>())
    val feedItems = _feedItems.asStateFlow()

    fun requestFeed() {
        viewModelScope.launch {
            bilibiliApi.getPopular(page).data?.let { res ->
                _feedItems.value = res.items
                page += 1
                hasMore = res.hasMore
            }
        }
    }

    fun requestMoreFeed() {
        if (!hasMore) { return }
        viewModelScope.launch {
            bilibiliApi.getPopular(page).data?.let { res ->
                _feedItems.value += res.items
                page += 1
            }
        }
    }
}