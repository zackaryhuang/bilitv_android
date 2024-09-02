package com.example.bilitv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.tv.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.example.bilitv.ui.theme.Bili_tv_androidTheme
import com.example.bilitv.view.AppNavigation
import com.example.bilitv.view.HomeScreen
import com.example.bilitv.view.LoginView
import com.example.bilitv.view.SplashView
import com.example.bilitv.view.model.LoginUserViewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jing.bilibilitv.http.data.UserInfo
import com.tencent.mmkv.MMKV
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginUserViewModel: LoginUserViewModel by viewModels()

    @OptIn(ExperimentalTvMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MMKV.initialize(this)
        loginUserViewModel.fetchUser()

        setContent {
            val userInfo = loginUserViewModel.userInfo.collectAsState()
            Bili_tv_androidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RectangleShape
                ) {
                    if (userInfo.value?.isLogin == true) {
//                        HomeScreen(userInfo.value!!)
                        AppNavigation(userInfo = userInfo.value!!, navController = rememberAnimatedNavController())
                    } else if (userInfo.value == null) {
                        SplashView()
                    } else {
                        LoginView(loginUserViewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}