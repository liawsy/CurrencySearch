package com.example.currencysearch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencysearch.viewmodel.model.CurrencyInfo
import com.example.currencysearch.viewmodel.model.CurrencyType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SearchViewModel: ViewModel() {

    private val _currencyTypes: MutableStateFlow<Set<CurrencyType>> = MutableStateFlow(
        setOf(CurrencyType.CRYPTO, CurrencyType.FIAT)
    )
    val currencyTypes = _currencyTypes.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _currencies = MutableStateFlow<List<CurrencyInfo>>(emptyList())
    val currencies =
        combine(_searchText, _currencyTypes, _currencies) { searchText, currencyTypes, currencies ->
            currencies.filterByAcceptedTypes(currencyTypes)
                .filterBySearch(searchText)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            _currencies.value
        )

    init {
        setIsLoading(true)
    }

    fun setCurrencies(currencies: List<CurrencyInfo>) {
        _currencies.value = currencies
    }

    fun setCurrencyTypes(currencyTypes: List<CurrencyType>) {
        _currencyTypes.value = currencyTypes.toSortedSet()
    }

    fun onSearchTextChanged(searchText: String) {
        _searchText.value = searchText
        _isSearching.value = true
    }

    fun cancelSearch() {
        _searchText.value = ""
        _isSearching.value = false
    }

    fun setIsLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    private fun List<CurrencyInfo>.filterByAcceptedTypes(currencyTypes: Set<CurrencyType>): List<CurrencyInfo> {
        val acceptsCrypto = currencyTypes.contains(CurrencyType.CRYPTO)
        val acceptsFiat = currencyTypes.contains(CurrencyType.FIAT)
        return this.filter { currencyInfo ->
            when (currencyInfo) {
                is CurrencyInfo.CryptoInfo -> acceptsCrypto
                is CurrencyInfo.FiatInfo -> acceptsFiat
             }
        }
    }

    private fun List<CurrencyInfo>.filterBySearch(searchText: String): List<CurrencyInfo> {
        return if (searchText.isBlank()) {
            this
        } else {
            this.filter { it.matchesQuery(searchText) }
        }
    }

}