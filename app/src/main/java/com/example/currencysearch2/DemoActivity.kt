package com.example.currencysearch2

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.currencysearch2.ui.screens.DemoCurrenciesScreen
import com.example.currencysearch2.ui.screens.InsertScreen
import com.example.currencysearch2.ui.theme.CurrencySearch2Theme

class DemoActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CurrencySearch2Theme {
                val navController = rememberNavController()
                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "demoCurrenciesScreen",
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent)
                    ) {
                        composable("demoCurrenciesScreen") { DemoCurrenciesScreen(navController) }
                        composable("insertScreen") { InsertScreen(navController) }
                    }
                }
            }
        }
    }
}

