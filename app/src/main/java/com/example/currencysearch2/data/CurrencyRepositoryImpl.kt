package com.example.currencysearch2.data

import com.example.currencysearch2.dao.CurrencyDao
import com.example.currencysearch2.domain.model.CurrencyInfo

class CurrencyRepositoryImpl(private val currencyDao: CurrencyDao): CurrencyRepository {

    override suspend fun insertCurrencies(currencies: List<CurrencyInfo>) {
        return currencyDao.upsertCurrencies(currencies.map { it.toCurrency() })
    }

}