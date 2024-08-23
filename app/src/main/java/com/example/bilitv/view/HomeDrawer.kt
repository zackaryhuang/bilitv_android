package com.example.bilitv.view

import androidx.annotation.ArrayRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*
import com.jing.bilibilitv.http.data.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun  HomeDrawer(
    userInfo: UserInfo,
    content: @Composable () -> Unit,
    selectedId: String,
    onMenuSelected: ((menuItem: MenuItem) -> Unit)?
) {
    val closeDrawerWidth = 80.dp
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var offsetState by remember { mutableStateOf(0.dp) }

    // 使用 animateDpAsState 为 offset 添加动画效果
    val animatedOffset by animateDpAsState(targetValue = offsetState)

    val drawerOpenWidth = 150.dp
    val drawerCloseWidth = 80.dp

    if (drawerState.currentValue == DrawerValue.Open) {
        offsetState = drawerOpenWidth - drawerCloseWidth
    } else {
        offsetState = 0.dp
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { drawer ->
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxHeight()
                    .requiredWidth(if (drawerState.currentValue == DrawerValue.Open) drawerOpenWidth else drawerCloseWidth)
                    .padding(12.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp, alignment = Alignment.CenterVertically
                ),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                DrawerItem(
                    expand = drawerState.currentValue == DrawerValue.Open,
                    item = MenuData.profile,
                    faceUrl = userInfo.face,
                    selected = selectedId == MenuData.profile.id,
                    onMenuSelected = {
                        drawerState.setValue(DrawerValue.Closed)
                        onMenuSelected?.invoke(MenuData.profile)
                    }
                )
                MenuData.menuItems.forEachIndexed { index, item ->
                    DrawerItem(
                        expand = drawerState.currentValue == DrawerValue.Open,
                        item = item,
                        selected = selectedId == item.id,
                        onMenuSelected = {
                            drawerState.setValue(DrawerValue.Closed)
                            onMenuSelected?.invoke(item)
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                DrawerItem(
                    expand = drawerState.currentValue == DrawerValue.Open,
                    item = MenuData.settingsItem,
                    selected = selectedId == MenuData.settingsItem.id,
                    onMenuSelected = {
                        drawerState.setValue(DrawerValue.Closed)
                        onMenuSelected?.invoke(MenuData.settingsItem)
                    }
                )
            }
        }, scrimBrush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.surface, Color.Transparent
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = closeDrawerWidth)
                .offset(
                    x = animatedOffset,
                    y = 0.dp
                )
        ) {
            content()
        }
    }
}

@Composable
fun DrawerItem(
    expand: Boolean,
    selected: Boolean,
    onMenuSelected: ((menuItem: MenuItem) -> Unit)?,
    faceUrl: String? = null,
    item: MenuItem
) {
    val interactionSource = remember { MutableInteractionSource() }
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
        onClick = {
            if (onMenuSelected != null) {
                onMenuSelected(item)
            }
        },
        colors = ClickableSurfaceDefaults.colors(containerColor = if (selected) Color.White.copy(alpha = 0.2f) else Color.Unspecified, focusedContainerColor = Color.White),
        interactionSource = interactionSource
    ) {
        Box(
            contentAlignment = if (expand) Alignment.CenterStart else Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(all = 5.dp)
                    .clickable {
                        if (onMenuSelected != null) {
                            onMenuSelected(item)
                        }
                    }
            ) {
                if (faceUrl != null) {
                    RemoteImage(
                        url = faceUrl,
                        Modifier.size(30.dp)
                            .clip(RoundedCornerShape(20.dp))
                    )
                } else {
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(item.icon),
                        contentDescription = item.text
                    )
                }
                
                if (expand) {
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        item.text,
                        color = if (isFocused) Color.Black else Color.White,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}