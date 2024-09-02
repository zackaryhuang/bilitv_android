package com.example.bilitv.view

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceBorder
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ClickableSurfaceScale
import androidx.tv.material3.ClickableSurfaceShape
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.bilitv.R
import com.example.bilitv.view.model.RecommendationScreenModel
import com.jing.bilibilitv.http.data.PlayableData
import com.jing.bilibilitv.http.data.VideoInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RecommendationScreen(
    onSelectVideo: (PlayableData) -> Unit
) {
    val listState = rememberLazyGridState()
    val viewModel: RecommendationScreenModel = hiltViewModel()
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
                onSelectVideo(dataItem)
            }) {
                DisplayableVideoItem(
                    item = dataItem
                )
            }
        }
    }
}


object CardItemDefaults {
    @OptIn(ExperimentalTvMaterial3Api::class)
    @ReadOnlyComposable
    @Composable
    fun border(borderRadius: Dp, color: Color) = ClickableSurfaceDefaults.border(
        focusedBorder = Border(
            BorderStroke(
                width = 2.dp, color = color
            ), shape = RoundedCornerShape(borderRadius)
        )
    )

    @ReadOnlyComposable
    @Composable
    fun shape(borderRadius: Dp) = ClickableSurfaceDefaults.shape(
        shape = RoundedCornerShape(borderRadius), focusedShape = RoundedCornerShape(borderRadius)
    )
}

@Composable
fun BorderedFocusableItem(
    modifier: Modifier = Modifier,
    borderRadius: Dp = 12.dp,
    scale: ClickableSurfaceScale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
    border: ClickableSurfaceBorder? = null,
    shape: ClickableSurfaceShape = CardItemDefaults.shape(borderRadius = borderRadius),
    color : Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable (BoxScope.() -> Unit)
) {
    val isFocused by interactionSource.collectIsFocusedAsState()

    val animatedFloat = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isFocused) {
        if (isFocused) {
            delay(1.seconds)
            animatedFloat.animateTo(
                targetValue = 0f, animationSpec = infiniteRepeatable(
                    animation = tween(700, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            animatedFloat.stop()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            scope.launch {
                animatedFloat.stop()
            }
        }
    }

    Surface(
        onClick = { onClick() },
        scale = scale,
        border = border ?: CardItemDefaults.border(borderRadius, MaterialTheme.colorScheme.inverseSurface.copy(alpha = animatedFloat.value)),
        shape = shape,
        colors = ClickableSurfaceDefaults.colors(containerColor = color, focusedContainerColor = color),
        modifier = modifier.fillMaxWidth(),
        interactionSource = interactionSource
    ) {
        content()
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