package com.example.currencysearch2.stubs

import com.example.currencysearch2.data.CurrencyRepository
import com.example.currencysearch2.data.CurrencySample
import com.example.currencysearch2.domain.model.CurrencyInfo

class CurrencyRepositoryStub : CurrencyRepository {

    companion object {
        val currencyList = listOf(CurrencyInfo.CryptoInfo(id = "BTC", name = "Bitcoin", symbol = "BTC"))
        const val CURRENCY_STRING = """[{"id":"BTC","name":"Bitcoin","symbol":"BTC"}]"""
    }

    override suspend fun insertCurrencies(currencies: List<CurrencyInfo>) = Unit

    override suspend fun clearCurrencies() = Unit

    override suspend fun getRawCurrencyString(currencySample: CurrencySample): String = CURRENCY_STRING

    override suspend fun getInitialData(): List<CurrencyInfo> = emptyList()

    override suspend fun getAllCurrencies(): List<CurrencyInfo> = currencyList

}