package com.example.currencysearch2.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencysearch2.domain.events.ButtonEvent
import com.example.currencysearch2.domain.model.CurrencyInfo
import com.example.currencysearch2.domain.model.CurrencyInfo.CryptoInfo
import com.example.currencysearch2.domain.model.CurrencyInfo.FiatInfo
import com.example.currencysearch2.domain.model.DisplayMode
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DemoViewModel: ViewModel() {

    private val _displayMode = MutableStateFlow(DisplayMode.ALL_CURRENCIES)
    val displayMode = _displayMode.asStateFlow()

    private val _buttonEvent = MutableSharedFlow<ButtonEvent>(1)
    val buttonEvent = _buttonEvent.asSharedFlow()

    private val testCurrencies = listOf(
        CryptoInfo(
            id = "1",
            name = "Bitcoin",
            symbol = "BTC",
        ),
        CryptoInfo(
            id = "2",
            name = "Ethereum",
            symbol = "ETH",
        ),
        CryptoInfo(
            id = "3",
            name = "XRP",
            symbol = "XRP"
        ),
        CryptoInfo(
            id = "BCH",
            name = "Bitcoin Cash",
            symbol = "BCH"
        ),
        FiatInfo(
            id = "SGD",
            name = "Singapore Dollar",
            symbol = "$",
            code = "SGD"
        ),
        FiatInfo(
            id = "GBP",
            name = "British Pound",
            symbol = "£",
            code = "GBP"
        ),
        FiatInfo(
            id = "USD",
            name = "UnitedStatesDollar",
            symbol = "$",
            code = "USD",
        )
    )

    init {
        viewModelScope.launch {
            _buttonEvent.emit(ButtonEvent.InsertCurrencies(testCurrencies))
        }
    }


    private val _currencies = MutableStateFlow<List<CurrencyInfo>>(testCurrencies)
    val currencies = _currencies.asStateFlow()

    fun toggleDisplayMode(displayMode: DisplayMode) {
        _displayMode.value = displayMode
        viewModelScope.launch {
            _buttonEvent.emit(ButtonEvent.SwitchDisplayMode(displayMode))
        }
    }

    fun clearCurrencies() {
        _currencies.value = emptyList()
        viewModelScope.launch {
            _buttonEvent.emit(ButtonEvent.ClearCurrencies)
        }
    }

    fun setCurrencies(currencies: List<CurrencyInfo>) {
        _currencies.value = currencies
    }

}