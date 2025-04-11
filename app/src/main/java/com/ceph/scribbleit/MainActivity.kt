package com.ceph.scribbleit

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ceph.scribbleit.presentation.homeScreen.HomeScreen
import com.ceph.scribbleit.presentation.homeScreen.HomeViewModel
import com.ceph.scribbleit.ui.theme.ScribbleItTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {

            var isDarkTheme by rememberSaveable { mutableStateOf(false) }

            val viewModel  = koinViewModel<HomeViewModel>()
            ScribbleItTheme(
                darkTheme = isDarkTheme
            ) {

               HomeScreen(
                   onToggleTheme = { isDarkTheme = !isDarkTheme },
                   isDarkTheme = isDarkTheme,
                   viewModel = viewModel
               )
            }
        }
    }
}
