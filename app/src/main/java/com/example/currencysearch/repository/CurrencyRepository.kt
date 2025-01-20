package com.example.currencysearch.repository

import com.example.currencysearch.viewmodel.model.CurrencyInfo

interface CurrencyRepository {

    suspend fun insertCurrencies(currencies: List<CurrencyInfo>)

    suspend fun clearCurrencies()

    suspend fun getRawCurrencyString(currencySample: CurrencySample): String

    suspend fun getInitialData(): List<CurrencyInfo>

    suspend fun getAllCurrencies(): List<CurrencyInfo>

}