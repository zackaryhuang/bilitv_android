package com.example.bilitv.view

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bilitv.R

sealed class NestedScreens(val title: String) {
    object Home : NestedScreens("home")
    object Search : NestedScreens("search")
    object Movies : NestedScreens("movies")
    object Songs : NestedScreens("songs")
    object Favorites : NestedScreens("favourites")
    object Settings : NestedScreens("settings")
}

object MenuData {
    val menuItems = listOf(
        MenuItem(NestedScreens.Home.title, "推荐", R.drawable.icon_recommend),
        MenuItem(NestedScreens.Search.title, "热门", R.drawable.icon_hot),
        MenuItem(NestedScreens.Movies.title, "直播", R.drawable.icon_live),
        MenuItem(NestedScreens.Songs.title, "排行榜", R.drawable.icon_rank),
        MenuItem(NestedScreens.Favorites.title, "关注", R.drawable.icon_follow),
    )

    val settingsItem = MenuItem(
        NestedScreens.Settings.title,
        "设置",
        R.drawable.icon_recommend,
    )

    val profile = MenuItem(
        NestedScreens.Home.title,
        "我的",
        R.drawable.icon_recommend,
    )
}

data class MenuItem(
    val id: String,
    val text: String,
    val icon: Int,
)

fun MenuItem.isCircleIcon(): Boolean = id == "search" || id == "Search"