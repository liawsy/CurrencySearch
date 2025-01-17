package com.example.currencysearch2.data

import com.example.currencysearch2.domain.model.CurrencyInfo

interface CurrencyRepository {

    suspend fun insertCurrencies(currencies: List<CurrencyInfo>)

}