package com.example.currencysearch2

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.JoinFull
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.fragment.compose.AndroidFragment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencysearch2.domain.DemoViewModel
import com.example.currencysearch2.domain.events.ButtonEvent
import com.example.currencysearch2.domain.model.CurrencyType
import com.example.currencysearch2.domain.model.DisplayMode
import com.example.currencysearch2.ui.components.ActionButtonInfo
import com.example.currencysearch2.ui.components.ActionButtonList
import com.example.currencysearch2.ui.fragments.CurrencyListFragment
import com.example.currencysearch2.ui.theme.CurrencySearch2Theme
import com.example.currencysearch2.utils.collectOnLifecycleStarted

class DemoActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = viewModel<DemoViewModel>()
            CurrencySearch2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(25.dp),
                        modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent)
                    ) {
                        ActionMenu()
                        AndroidFragment<CurrencyListFragment>(modifier = Modifier.padding(bottom = 10.dp)) { fragment ->
                            // TODO: Restore last display mode and pass to fragment
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

                                    is ButtonEvent.InsertCurrencies -> fragment.updateCurrencies(
                                        buttonEvent.currencies
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ActionMenu(modifier: Modifier = Modifier) {
        val viewModel = viewModel<DemoViewModel>()
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
                        // TODO
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
}

