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
        MenuItem(NestedScreens.Home.title, "Home", R.drawable.icon_recommend),
        MenuItem(NestedScreens.Search.title, "Search", R.drawable.icon_recommend),
        MenuItem(NestedScreens.Movies.title, "Movies", R.drawable.icon_recommend),
        MenuItem(NestedScreens.Songs.title, "Songs", R.drawable.icon_recommend),
        MenuItem(NestedScreens.Favorites.title, "Favorites", R.drawable.icon_recommend),
    )

    val settingsItem = MenuItem(
        NestedScreens.Settings.title,
        "Settings",
        R.drawable.icon_recommend,
    )

    val profile = MenuItem(
        NestedScreens.Home.title,
        "My Profile",
        R.drawable.icon_recommend,
    )
}

data class MenuItem(
    val id: String,
    val text: String,
    val icon: Int,
)

fun MenuItem.isCircleIcon(): Boolean = id == "search" || id == "Search"