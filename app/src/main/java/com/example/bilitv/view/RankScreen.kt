package com.example.bilitv.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Surface
import com.example.bilitv.R
import com.example.bilitv.view.model.FollowScreenModel
import com.example.bilitv.view.model.RankScreenModel
import com.jing.bilibilitv.http.data.DynamicItem
import com.jing.bilibilitv.http.data.HotVideInfo
import com.jing.bilibilitv.http.data.SeasonRankVideoInfo

@Composable
fun RankScreen() {
    val state = remember { mutableStateOf(0) }
    val listState = rememberLazyGridState()
    val viewModel: RankScreenModel = hiltViewModel()
    val dataItems = viewModel.feedItems.collectAsState()
    val seasonDataItems = viewModel.seasonFeedItems.collectAsState()
    LaunchedEffect(listState) {
        if (dataItems.value.isEmpty()) {
            viewModel.requestFeed(RankCategoryInfo.All[state.value])
        }
    }
    Column {
        Spacer(modifier = Modifier.height(20.dp))
        ScrollableTabRow(
            selectedTabIndex = state.value,
            containerColor = Color.Unspecified,
            indicator = { },
            divider = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp),
            edgePadding = 0.dp
        ) {
            RankCategoryInfo.All.forEachIndexed { index, rankCategoryInfo ->
                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .fillMaxSize(),
                    onClick = {
                        state.value = index
                        viewModel.requestFeed(RankCategoryInfo.All[state.value])
                    },
                    colors = ClickableSurfaceDefaults.colors(containerColor = if (index == state.value) Color(0xFFFF6699) else Color.Unspecified, focusedContainerColor = if (index == state.value) Color(0xFFFF6699) else Color.White.copy(0.3F))
                ) {
                    Box(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            text = rankCategoryInfo.title,
                            lineHeight = 30.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyVerticalGrid(
            state = listState,
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            columns = GridCells.Fixed(4)
        ) {
            if (RankCategoryInfo.All[state.value].isSeason) {
                items(seasonDataItems.value.count()) { index ->
                    val dataItem = seasonDataItems.value[index]
                    BorderedFocusableItem(onClick = {
//                onSelectVideo(dataItem)
                    }) {
                        DisplayableVideoItem(
                            item = dataItem,
                            onClick = { item ->
                                println(item.title)
                            }
                        )
                    }
                }
            } else {
                items(dataItems.value.count()) { index ->
                    val dataItem = dataItems.value[index]
                    BorderedFocusableItem(onClick = {
//                onSelectVideo(dataItem)
                    }) {
                        DisplayableVideoItem(
                            item = dataItem,
                            onClick = { item ->
                                println(item.title)
                            }
                        )
                    }
                }
            }

        }
    }
}