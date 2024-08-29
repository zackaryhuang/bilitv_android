package com.example.bilitv.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bilitv.view.RankCategoryInfo
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.data.HotVideInfo
import com.jing.bilibilitv.http.data.SeasonRankVideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RankScreenModel @Inject constructor(
    private val bilibiliApi: BilibiliApi,
) : ViewModel() {

    private val _feedItems = MutableStateFlow<List<HotVideInfo>>(value = mutableListOf<HotVideInfo>())
    val feedItems = _feedItems.asStateFlow()

    private val _seasonFeedItems = MutableStateFlow<List<SeasonRankVideoInfo>>(value = mutableListOf<SeasonRankVideoInfo>())
    val seasonFeedItems = _seasonFeedItems.asStateFlow()

    fun requestFeed(categoryInfo: RankCategoryInfo) {
        viewModelScope.launch {
            if (categoryInfo.isSeason) {
                bilibiliApi.getSeasonRank(categoryInfo.rid).result?.let { res ->
                    _seasonFeedItems.value = res.items
                }
            } else {
                bilibiliApi.getRank(categoryInfo.rid).data?.let {  data ->
                    _feedItems.value = data.items
                }
            }

        }
    }
}