package com.example.bilitv.view

import android.provider.MediaStore.Video
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.bilitv.view.model.tabEnterTransition
import com.example.bilitv.view.model.tabExitTransition
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.jing.bilibilitv.http.data.UserInfo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(
    userInfo: UserInfo,
    navController: NavHostController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = ScreenRoute.NestHome.route
    ) {
        // e.g will add auth routes here if when we will extend project
        composable(
            ScreenRoute.NestHome.route,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }
        ) {
            HomeScreen(userInfo = userInfo, onSelectPlayableData = { playableData ->
                if (playableData.roomID != null) {
                    var link = ScreenRoute.LivePlayer.route
                    link += ("/" + (playableData.roomID ?: "null"))
                    navController.navigate(link)
                } else {
                    var link = ScreenRoute.VideoPlayer.route
                    link += ("/" + (playableData.aid ?: "null"))
                    link += ("/" + (playableData.bvid ?: "null"))
                    link += ("/" + (playableData.cid ?: "null"))
                    link += ("/" + (playableData.seasonID ?: "null"))
                    link += ("/" + (playableData.episodeID ?: "null"))
                    navController.navigate(link)
                }
            })
        }

        composable(
            ScreenRoute.VideoDetail.routeWithArgument,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() },
            arguments = listOf(
                navArgument(ScreenRoute.VideoDetail.aid) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoDetail.cid) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoDetail.bvid) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoDetail.seasonID) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoDetail.episodeID) {
                    type = NavType.StringType; nullable = true
                },
            )
        ) { backStackEntry ->
            val cid = backStackEntry.arguments?.getString(ScreenRoute.VideoDetail.cid) ?: ""

            val aid = backStackEntry.arguments?.getString(ScreenRoute.VideoDetail.aid)

            val bvid = backStackEntry.arguments?.getString(ScreenRoute.VideoDetail.bvid)

            val seasonID = backStackEntry.arguments?.getString(ScreenRoute.VideoDetail.seasonID)

            val episodeID = backStackEntry.arguments?.getString(ScreenRoute.VideoDetail.episodeID)

            if (aid != null) {
                VideoDetailScreen(aid = aid, cid = cid)
            } else if (bvid != null) {
                VideoDetailScreen(bvid = bvid, cid = cid)
            } else {
                VideoDetailScreen(cid = cid)
            }
        }

        composable(
            ScreenRoute.VideoPlayer.routeWithArgument,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() },
            arguments = listOf(
                navArgument(ScreenRoute.VideoPlayer.aid) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoPlayer.bvid) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoPlayer.cid) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoPlayer.seasonID) {
                    type = NavType.StringType; nullable = true
                },
                navArgument(ScreenRoute.VideoPlayer.episodeID) {
                    type = NavType.StringType; nullable = true
                },
            )
        ) { backStackEntry ->

            val cid = backStackEntry.arguments?.getString(ScreenRoute.VideoPlayer.cid)

            val aid = backStackEntry.arguments?.getString(ScreenRoute.VideoPlayer.aid)

            val bvid = backStackEntry.arguments?.getString(ScreenRoute.VideoPlayer.bvid)

            val seasonID = backStackEntry.arguments?.getString(ScreenRoute.VideoPlayer.seasonID)

            val episodeID = backStackEntry.arguments?.getString(ScreenRoute.VideoPlayer.episodeID)

            VideoPlayerScreen(PlayData(aid, bvid, cid, seasonID, episodeID))
        }

        composable(
            ScreenRoute.LivePlayer.routeWithArgument,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() },
            arguments = listOf(
                navArgument(ScreenRoute.LivePlayer.roomID) {
                    type = NavType.StringType; nullable = true
                },
            )
        ) { backStackEntry ->
            val roomID = backStackEntry.arguments?.getString(ScreenRoute.LivePlayer.roomID)
            roomID?.let {
                LivePlayerScreen(roomID = it)
            }
        }
    }
}