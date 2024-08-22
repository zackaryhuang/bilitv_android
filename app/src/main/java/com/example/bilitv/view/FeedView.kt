package com.example.bilitv.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.rememberTvLazyGridState
import androidx.tv.material3.Text
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.bilitv.view.model.FeedViewModel
import com.jing.bilibilitv.http.data.VideoInfo

@Composable
fun FeedView(modifier: Modifier = Modifier, onSelectVideo: (VideoInfo) -> Unit) {
    val listState = rememberTvLazyGridState()
    val viewModel: FeedViewModel = hiltViewModel()
    val dataItems = viewModel.feedItems.collectAsState()
    LaunchedEffect(listState) {
        if (dataItems.value.isEmpty()) {
            viewModel.requestFeed()
        }

        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (dataItems.value.isNotEmpty() &&
                    lastVisibleItemIndex == dataItems.value.size - 1) {
                    viewModel.requestMoreFeed()
                }
            }
    }

    TvLazyVerticalGrid(
        state = listState,
        modifier = Modifier
            .fillMaxWidth(),
        columns = TvGridCells.Fixed(4),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)

    ) {
        items(dataItems.value.count()) { index ->
            val dataItem = dataItems.value[index]
            Box(
                modifier = Modifier
                    .focusable()
            ) {
                DataItem(
                    modifier = Modifier,
                    item = dataItem,
                    onClick = { item ->
                        println(item.title)
                    }
                )
            }
        }
    }
}

@Composable
fun DataItem(
    modifier: Modifier,
    item: VideoInfo,
    onClick: ((VideoInfo) -> Unit)? = null) {
    Box(
        modifier = Modifier
            .height(240.dp)
            .clickable {
                onClick?.let {
                    it(item)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .padding(all = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    RemoteImage(
                        url = item.pic,
                        modifier = Modifier
                            .size(width = 200.dp, height = 130.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Spacer(
                        modifier = Modifier
                            .height(10.dp)
                    )
                    Text(
                        text = item.title,
                        maxLines = 2,
                        color = Color.LightGray
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RemoteImage(
                        url = item.owner.face,
                        modifier = Modifier
                            .size(width = 20.dp, height = 20.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )

                    Text(
                        text = item.owner.name,
                        color = Color.Gray,
                    )
                }
            }
        }
    }
}

@Composable
fun RemoteImage(url: String, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        onState = { state ->
            Log.d("huangchao01", "${state}, ${url}")
        }
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}