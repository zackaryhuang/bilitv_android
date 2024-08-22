package com.example.bilitv.view

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.*

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun  HomeDrawer(
    content: @Composable () -> Unit,
    selectedId: String = MenuData.menuItems.first().id,
    onMenuSelected: ((menuItem: MenuItem) -> Unit)?
) {
    val closeDrawerWidth = 80.dp
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var offsetState by remember { mutableStateOf(0.dp) }

    // 使用 animateDpAsState 为 offset 添加动画效果
    val animatedOffset by animateDpAsState(targetValue = offsetState)

    if (drawerState.currentValue == DrawerValue.Open) {
        offsetState = 170.dp
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
                    .requiredWidth(if (drawerState.currentValue == DrawerValue.Open) 250.dp else 80.dp)
                    .padding(12.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp, alignment = Alignment.CenterVertically
                ),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Header(
                    item = MenuData.profile,
                    onMenuSelected = {
                        drawerState.setValue(DrawerValue.Closed)
                    }
                )
                MenuData.menuItems.forEachIndexed { index, item ->
                    NavigationRow(
                        item = item,
                        isSelected = selectedId == item.id,
                        onMenuSelected = {
                            drawerState.setValue(DrawerValue.Closed)
                            onMenuSelected?.invoke(item)
                        }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                NavigationRow(
                    item = MenuData.settingsItem,
                    isSelected = selectedId == MenuData.settingsItem.id,
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
fun NavigationDrawerScope.NavigationRow(
    item: MenuItem,
    isSelected: Boolean,
    enabled: Boolean = true,
    onMenuSelected: ((menuItem: MenuItem) -> Unit)?
) {
    NavigationDrawerItem(
        selected = isSelected,
        enabled = enabled,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            ),
            selectedContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        onClick = {
            onMenuSelected?.invoke(item)
        },
        leadingContent = {
            Image(
                painter = painterResource(item.icon),
                contentDescription = item.text
            )
        }
    ) {
        Text(item.text)
    }
}

@Composable
fun NavigationDrawerScope.Header(
    item: MenuItem, onMenuSelected: ((menuItem: MenuItem) -> Unit)?
) {
    NavigationDrawerItem(selected = false, onClick = {
        onMenuSelected?.invoke(item)
    }, leadingContent = {
        Image(
            painter = painterResource(item.icon),
            contentDescription = item.text,
            modifier = Modifier.size(40.dp),
        )
    }) {
        Text(item.text)
    }
}