package com.example.bilitv.view

import com.example.bilitv.R

sealed class NestedScreens(val title: String) {
    object Recommendation : NestedScreens("Recommendation")
    object Hot : NestedScreens("Hot")
    object Live : NestedScreens("Live")
    object Rank : NestedScreens("Rank")
    object Follow : NestedScreens("Follow")
    object Settings : NestedScreens("Settings")
    object Profile : NestedScreens("Profile")
}

object MenuData {
    val menuItems = listOf(
        MenuItem(NestedScreens.Recommendation.title, "推荐", R.drawable.icon_recommend),
        MenuItem(NestedScreens.Hot.title, "热门", R.drawable.icon_hot),
        MenuItem(NestedScreens.Live.title, "直播", R.drawable.icon_live),
        MenuItem(NestedScreens.Rank.title, "排行榜", R.drawable.icon_rank),
        MenuItem(NestedScreens.Follow.title, "关注", R.drawable.icon_follow),
    )

    val settingsItem = MenuItem(
        NestedScreens.Settings.title,
        "设置",
        R.drawable.icon_recommend,
    )

    val profile = MenuItem(
        NestedScreens.Profile.title,
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