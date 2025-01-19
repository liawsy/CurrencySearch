package com.example.currencysearch2.domain.events

import com.example.currencysearch2.domain.model.CurrencyInfo
import com.example.currencysearch2.domain.model.DisplayMode

sealed interface ButtonEvent {
    data object ClearCurrencies: ButtonEvent
    data class SwitchDisplayMode(val displayMode: DisplayMode): ButtonEvent
    data class InsertCurrenciesComplete(val currencies: List<CurrencyInfo>): ButtonEvent
    data object InsertingCurrencies: ButtonEvent
}