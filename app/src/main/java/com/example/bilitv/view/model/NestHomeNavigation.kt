package com.example.bilitv.view.model

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.bilitv.view.FollowScreen
import com.example.bilitv.view.HotScreen
import com.example.bilitv.view.LiveScreen
import com.example.bilitv.view.NestedScreens
import com.example.bilitv.view.ProfileScreen
import com.example.bilitv.view.RankScreen
import com.example.bilitv.view.RecommendationScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jing.bilibilitv.http.data.PlayableData

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NestedHomeScreenNavigation(
    navController: NavHostController,
    onSelectPlayableData: (PlayableData) -> Unit
) {
    AnimatedNavHost(navController = navController, startDestination = NestedScreens.Recommendation.title) {
        // e.g will add auth routes here if when we will extend project
        composable(
            NestedScreens.Recommendation.title,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }) {
            RecommendationScreen() { playableVideo ->
                onSelectPlayableData(playableVideo)
            }
        }

        composable(
            NestedScreens.Hot.title,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }) {
            HotScreen()
        }

        composable(
            NestedScreens.Live.title,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }) {
            LiveScreen()
        }

        composable(
            NestedScreens.Rank.title,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }) {
            RankScreen(onSelectVideo = {

            })
        }

        composable(
            NestedScreens.Follow.title,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }) {
            FollowScreen()
        }
        composable(
            NestedScreens.Profile.title,
            enterTransition = { tabEnterTransition() },
            exitTransition = { tabExitTransition() }) {
            ProfileScreen()
        }
    }
}

fun tabExitTransition(
    duration: Int = 500,
) = fadeOut(tween(duration / 2, easing = LinearEasing))

fun tabEnterTransition(
    duration: Int = 500,
    delay: Int = duration - 350,
) = fadeIn(tween(duration, duration - delay))