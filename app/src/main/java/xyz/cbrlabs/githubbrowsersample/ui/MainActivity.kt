package xyz.cbrlabs.githubbrowsersample.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import xyz.cbrlabs.githubbrowsersample.ui.components.AppNavigationGraph
import xyz.cbrlabs.githubbrowsersample.ui.theme.GithubBrowserSampleTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            GithubBrowserSampleTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    AppNavigationGraph()
}