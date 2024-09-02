package com.example.bilitv.view

import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
//import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.example.bilitv.R
import com.example.bilitv.view.model.NestedHomeScreenNavigation
import com.example.bilitv.view.model.tabEnterTransition
import com.example.bilitv.view.model.tabExitTransition
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jing.bilibilitv.http.data.PlayableData
import com.jing.bilibilitv.http.data.UserInfo

//@Preview
@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun HomeScreen(
    userInfo: UserInfo,
    onSelectPlayableData: (PlayableData) -> Unit
){
    val selectedIDState = remember {
        mutableStateOf(value = MenuData.menuItems.first().id)
    }

    val navController = rememberAnimatedNavController()

    HomeDrawer(
        userInfo = userInfo,
        selectedId = selectedIDState.value,
        content = {
            NestedHomeScreenNavigation(navController = navController, onSelectPlayableData = { playableData ->
                onSelectPlayableData(playableData)
            })
    }) { menuItem ->
        selectedIDState.value = menuItem.id
        navController.navigate(menuItem.id)
    }
}

@Composable
fun CenterText(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, fontSize = 32.sp)
    }
}

data class SidePanelItem(
    val name: String,
    @DrawableRes val image: Int
)