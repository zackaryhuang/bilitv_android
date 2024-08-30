package com.example.bilitv.view.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bilitv.view.LiveCategoryInfo
import com.jing.bilibilitv.http.api.LiveApi
import com.jing.bilibilitv.http.data.DisplayableData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveScreenModel @Inject constructor(
    private val liveApi: LiveApi,
) : ViewModel() {

    private val _feedItems = MutableStateFlow<List<DisplayableData>>(value = mutableListOf<DisplayableData>())
    val feedItems = _feedItems.asStateFlow()

    var page = 1

    fun requestFeed(categoryInfo: LiveCategoryInfo) {
        viewModelScope.launch {
            page = 1
            when (categoryInfo.areaID) {
                null -> { // 关注
                    liveApi.getFollowedLiveRoom(page).data?.rooms?.let { followedLiveRooms ->
                        _feedItems.value = followedLiveRooms
                        page += 1
                    }
                }

                0 -> { // 热门
                    liveApi.getHotLiveRoom(page).data?.list?.let { hotLiveRooms ->
                        _feedItems.value = hotLiveRooms
                        page += 1
                    }
                }

                -1 -> { // 推荐
                    liveApi.getRecommendLiveRoom(page).data?.list?.let { recommendLiveRoom ->
                        _feedItems.value = recommendLiveRoom
                        page += 1
                    }
                }

                else -> {
                    liveApi.getAreaLiveRoom(parentAreaId = categoryInfo.areaID.toString(), page = page).data?.list?.let { areaLiveRooms ->
                        _feedItems.value = areaLiveRooms
                        page += 1
                    }
                }
            }

        }
    }

    fun requestMoreFeed(categoryInfo: LiveCategoryInfo) {
        viewModelScope.launch {
            when (categoryInfo.areaID) {
                null -> {
                    liveApi.getFollowedLiveRoom(page).data?.rooms?.let { followedLiveRooms ->
                        _feedItems.value += followedLiveRooms
                        page += 1
                    }
                }

                0 -> {
                    liveApi.getHotLiveRoom(page).data?.list?.let { hotLiveRooms ->
                        _feedItems.value += hotLiveRooms
                        page += 1
                    }
                }

                -1 -> {
                    liveApi.getRecommendLiveRoom(page).data?.list?.let { recommendLiveRoom ->
                        _feedItems.value += recommendLiveRoom
                        page += 1
                    }
                }

                else -> {
                    liveApi.getAreaLiveRoom(parentAreaId = categoryInfo.areaID.toString(), page = page).data?.list?.let { areaLiveRooms ->
                        _feedItems.value += areaLiveRooms
                        page += 1
                    }
                }
            }
        }
    }
}