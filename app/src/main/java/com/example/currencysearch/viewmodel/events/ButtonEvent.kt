package com.example.currencysearch.viewmodel.events

import com.example.currencysearch.viewmodel.model.CurrencyInfo
import com.example.currencysearch.viewmodel.model.DisplayMode

sealed interface ButtonEvent {
    data object ClearCurrencies: ButtonEvent
    data class SwitchDisplayMode(val displayMode: DisplayMode): ButtonEvent
    data class InsertCurrenciesComplete(val currencies: List<CurrencyInfo>): ButtonEvent
    data object InsertingCurrencies: ButtonEvent
}