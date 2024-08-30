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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import com.example.bilitv.R
import com.example.bilitv.view.model.HotScreenModel
import com.jing.bilibilitv.http.data.DisplayableData

@Composable
fun HotScreen() {
    val listState = rememberLazyGridState()
    val viewModel: HotScreenModel = hiltViewModel()
    val dataItems = viewModel.feedItems.collectAsState()
    LaunchedEffect(listState) {
        if (dataItems.value.isEmpty()) {
            viewModel.requestFeed()
        }

        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
        }.collect { lastVisibleItemIndex ->
            if (dataItems.value.isNotEmpty() &&
                lastVisibleItemIndex == dataItems.value.size - 1) {
                viewModel.requestMoreFeed()
            }
        }
    }

    LazyVerticalGrid(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        columns = GridCells.Fixed(4)
    ) {
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