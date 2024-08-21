package com.example.bilitv.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Text
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.bilitv.view.model.FeedViewModel
import com.jing.bilibilitv.http.data.VideoInfo

@Composable
fun FeedView(modifier: Modifier) {
    val listState = rememberLazyGridState()
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

    Column {
        LazyVerticalGrid(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray),
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ) {
            items(dataItems.value.count()) { index ->
                val dataItem = dataItems.value[index]
                DataItem(dataItem, onClick = { item ->
                    println(item.title)
                })
            }
        }
    }
}

@Composable
fun DataItem(item: VideoInfo, onClick: ((VideoInfo) -> Unit)? = null) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .size(width = 190.dp, height = 220.dp)
            .clickable {
                onClick?.let {
                    it(item)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .padding(all = 10.dp)
                .background(Color.White)
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
                    )
                    Text(
                        text = item.title,
                        maxLines = 2,
                        color = Color.Black
                    )
                }

                Text(
                    text = item.owner.name,
                    color = Color.Gray,
                )
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