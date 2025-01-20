package com.example.currencysearch

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencysearch.ui.screens.DemoCurrenciesScreen
import com.example.currencysearch.ui.screens.InsertScreen
import com.example.currencysearch.ui.theme.CurrencySearchTheme

class DemoActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            CurrencySearchTheme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "demoCurrenciesScreen",
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent),
                        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) + fadeIn() },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) + fadeOut() },
                        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) + fadeIn() },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) + fadeOut() }
                    ) {
                        composable("demoCurrenciesScreen") { DemoCurrenciesScreen(navController) }
                        composable("insertScreen") { InsertScreen(navController) }
                    }
                }
            }
        }
    }
}

