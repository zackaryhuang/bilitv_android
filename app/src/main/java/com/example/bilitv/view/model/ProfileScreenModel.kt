package com.example.bilitv.view.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jing.bilibilitv.http.api.BilibiliApi
import com.jing.bilibilitv.http.data.HistoryItem
import com.jing.bilibilitv.http.data.UserInfo
import com.jing.bilibilitv.http.data.WatchLaterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenModel @Inject constructor(
    private val bilibiliApi: BilibiliApi,
) : ViewModel() {
    private val _recentViewedItems = MutableStateFlow<List<HistoryItem>>(value = mutableListOf<HistoryItem>())
    val recentViewedItems = _recentViewedItems.asStateFlow()

    private val _watchLaterItems = MutableStateFlow<List<WatchLaterItem>>(value = mutableListOf<WatchLaterItem>())
    val watchLaterItems = _watchLaterItems.asStateFlow()

    private val _loginInfo = MutableStateFlow<UserInfo?>(value = null)
    val loginInfo = _loginInfo.asStateFlow()

    var max: Long = 0
    var viewAt: Long = 0

    fun requestLoginInfo() {
        viewModelScope.launch {
            bilibiliApi.getLoginUserInfo().data?.let { userInfo ->
                _loginInfo.value = userInfo
            }
        }
    }

    fun requestRecent() {
        viewModelScope.launch {
            bilibiliApi.getHistory(max,viewAt, "archive", "archive").data?.let { res ->
                _recentViewedItems.value = res.list
                max = res.cursor.max
                viewAt = res.cursor.viewAt
            }
        }
    }

    fun requestMoreRecent() {
        viewModelScope.launch {
            bilibiliApi.getHistory(max,viewAt, "archive", "archive").data?.let { res ->
                _recentViewedItems.value += res.list
                max = res.cursor.max
                viewAt = res.cursor.viewAt
            }
        }
    }

    fun requestWatchLater() {
        viewModelScope.launch {
            bilibiliApi.getWatchLater().data?.list?.let { list ->
                _watchLaterItems.value = list
            }
        }
    }
}