package com.example.bilitv.view

import com.example.bilitv.R

class LiveCategoryInfo(val title: String, val areaID: Int?) {
    companion object {
        val All: Array<LiveCategoryInfo> = arrayOf(
            LiveCategoryInfo("关注", null),
            LiveCategoryInfo("推荐", -1),
//            LiveCategoryInfo("热门", 0),
            LiveCategoryInfo("娱乐", 1),
            LiveCategoryInfo("网游", 2),
            LiveCategoryInfo("手游", 3),
            LiveCategoryInfo("电台", 5),
            LiveCategoryInfo("单机", 6),
            LiveCategoryInfo("虚拟", 9),
            LiveCategoryInfo("生活", 10),
            LiveCategoryInfo("知识", 11),
            LiveCategoryInfo("竞技", 13),
        )
    }
}

class RankCategoryInfo(val title: String, val rid: Int, val isSeason: Boolean = false) {
    companion object {
        val All: Array<RankCategoryInfo> = arrayOf(
            RankCategoryInfo("全站", 0),
            RankCategoryInfo("动画", 1),
            RankCategoryInfo("番剧", 1, true),
            RankCategoryInfo("音乐", 3),
            RankCategoryInfo("国创", 4, true),
            RankCategoryInfo("游戏", 4),
            RankCategoryInfo("娱乐", 5),
            RankCategoryInfo("电视剧", 11),
            RankCategoryInfo("电影", 23),
            RankCategoryInfo("知识", 36),
            RankCategoryInfo("科技", 188),
            RankCategoryInfo("运动", 234),
            RankCategoryInfo("汽车", 223),
            RankCategoryInfo("生活", 160),
            RankCategoryInfo("美食", 211),
            RankCategoryInfo("动物圈", 217),
            RankCategoryInfo("鬼畜", 119),
            RankCategoryInfo("时尚", 155),
            RankCategoryInfo("影视", 181),
            RankCategoryInfo("纪录片", 177),
        )
    }
}

sealed class NestedScreens(val title: String) {
    object Recommendation : NestedScreens("Recommendation")
    object Hot : NestedScreens("Hot")
    object Live : NestedScreens("Live")
    object Rank : NestedScreens("Rank")
    object Follow : NestedScreens("Follow")
    object Settings : NestedScreens("Settings")
    object Profile : NestedScreens("Profile")
}

sealed class ScreenRoute(val route: String) {
    object NestHome : ScreenRoute("NestHome")
    object VideoDetail : ScreenRoute("VideoDetail") {
        const val routeWithArgument: String = "VideoDetail/{aid}/{bvid}/{cid}"
        const val aid: String = "aid"
        const val bvid: String = "bvid"
        const val cid: String = "cid"
    }
}

object MenuData {
    val menuItems = listOf(
        MenuItem(NestedScreens.Recommendation.title, "推荐", R.drawable.icon_recommend),
        MenuItem(NestedScreens.Hot.title, "热门", R.drawable.icon_hot),
        MenuItem(NestedScreens.Live.title, "直播", R.drawable.icon_live),
        MenuItem(NestedScreens.Rank.title, "排行榜", R.drawable.icon_rank),
        MenuItem(NestedScreens.Follow.title, "关注", R.drawable.icon_follow),
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