package com.example.currencysearch.repository

import android.content.Context
import androidx.core.content.edit
import com.example.currencysearch.database.CurrencyDao
import com.example.currencysearch.viewmodel.model.CurrencyInfo
import kotlinx.serialization.json.Json

class CurrencyRepositoryImpl(private val context: Context, private val currencyDao: CurrencyDao): CurrencyRepository {

    companion object {
        private const val CURRENCY_SP = "currency_shared_pref"
        private const val HAS_OPENED_APP_BEFORE = "has_opened_app_before"
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(CURRENCY_SP, Context.MODE_PRIVATE)
    }

    override suspend fun insertCurrencies(currencies: List<CurrencyInfo>) {
        return currencyDao.upsertCurrencies(currencies.map { it.toCurrency() })
    }

    override suspend fun clearCurrencies() {
        return currencyDao.clearAll()
    }

    override suspend fun getRawCurrencyString(currencySample: CurrencySample): String {
        val fileName = when (currencySample) {
            CurrencySample.DATASET_A -> "currencyListA.json"
            CurrencySample.DATASET_B -> "currencyListB.json"
            CurrencySample.BOTH -> "combinedCurrencyList.json"
        }
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        return jsonString
    }

    override suspend fun getInitialData(): List<CurrencyInfo> {
        val hasOpenedAppBefore = sharedPreferences.getBoolean(HAS_OPENED_APP_BEFORE, false)
        if (!hasOpenedAppBefore) {
            val currencyStringToInsert = getRawCurrencyString(CurrencySample.BOTH)
            val currencies = Json.decodeFromString<List<CurrencyInfoDTO>>(currencyStringToInsert).map { it.toCurrencyInfo() }
            insertCurrencies(currencies)
            sharedPreferences.edit {
                putBoolean(HAS_OPENED_APP_BEFORE, true)
            }
            return currencies
        } else {
            return getAllCurrencies()
        }
    }

    override suspend fun getAllCurrencies(): List<CurrencyInfo> {
        return currencyDao.getAll().map { it.toCurrencyInfo() }
    }

}