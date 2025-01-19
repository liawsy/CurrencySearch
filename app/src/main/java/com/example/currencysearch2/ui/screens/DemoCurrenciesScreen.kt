package com.example.currencysearch2.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.JoinFull
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.compose.AndroidFragment
import androidx.navigation.NavController
import com.example.currencysearch2.domain.DemoViewModel
import com.example.currencysearch2.domain.events.ButtonEvent
import com.example.currencysearch2.domain.model.CurrencyType
import com.example.currencysearch2.domain.model.DisplayMode
import com.example.currencysearch2.ui.components.ActionButtonInfo
import com.example.currencysearch2.ui.components.ActionButtonList
import com.example.currencysearch2.ui.fragments.CurrencyListFragment
import com.example.currencysearch2.utils.collectOnLifecycleStarted
import org.koin.androidx.compose.koinViewModel

@Composable
fun DemoCurrenciesScreen(navController: NavController) {
    val viewModel = koinViewModel<DemoViewModel>(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(25.dp),
    ) {
        ActionMenu(navController)
        AndroidFragment<CurrencyListFragment>(modifier = Modifier.padding(bottom = 10.dp)) { fragment ->
            viewModel.buttonEvent.collectOnLifecycleStarted(fragment) { buttonEvent ->
                when (buttonEvent) {
                    ButtonEvent.ClearCurrencies -> fragment.updateCurrencies(
                        emptyList()
                    )

                    is ButtonEvent.SwitchDisplayMode -> {
                        val currencyTypes = when (buttonEvent.displayMode) {
                            DisplayMode.ALL_CURRENCIES -> listOf(
                                CurrencyType.CRYPTO,
                                CurrencyType.FIAT
                            )

                            DisplayMode.CRYPTO -> listOf(CurrencyType.CRYPTO)
                            DisplayMode.FIAT -> listOf(CurrencyType.FIAT)
                        }
                        fragment.setDisplayCurrencies(currencyTypes)
                    }

                    is ButtonEvent.InsertCurrenciesComplete -> {
                        fragment.setIsLoading(false)
                        fragment.updateCurrencies(
                            buttonEvent.currencies
                        )
                    }
                    is ButtonEvent.InsertingCurrencies -> fragment.setIsLoading(true)
                }
            }
        }
    }
}

@Composable
fun ActionMenu(navController: NavController, modifier: Modifier = Modifier) {
    val viewModel = koinViewModel<DemoViewModel>(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    Column(modifier = modifier.animateContentSize()) {
        Text(
            text = "Actions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(15.dp))
        ActionButtonList(
            listOf(
                ActionButtonInfo(
                    Icons.Filled.Delete,
                    text = "Clear",
                    onClick = { viewModel.clearCurrencies() }),
                ActionButtonInfo(Icons.Filled.Add, text = "Insert", onClick = {
                    navController.navigate("insertScreen")
                }),
                ActionButtonInfo(
                    Icons.Filled.CurrencyBitcoin,
                    text = "Crypto",
                    onClick = { viewModel.toggleDisplayMode(DisplayMode.CRYPTO) }),
                ActionButtonInfo(
                    Icons.Filled.AttachMoney,
                    text = "Fiat",
                    onClick = { viewModel.toggleDisplayMode(DisplayMode.FIAT) }),
                ActionButtonInfo(
                    Icons.Filled.JoinFull,
                    text = "Both",
                    onClick = { viewModel.toggleDisplayMode(DisplayMode.ALL_CURRENCIES) }),
            )
        )
    }
}