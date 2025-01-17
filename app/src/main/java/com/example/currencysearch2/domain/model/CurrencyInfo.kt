package com.example.currencysearch2.domain.model

import com.example.currencysearch2.dao.Currency
import com.example.currencysearch2.dao.CurrencyType

sealed class CurrencyInfo(
    open val id: String,
    open val name: String,
    open val symbol: String,
    open val iconUrl: String?
) {
    data class CryptoInfo(
        override val id: String,
        override val name: String,
        override val symbol: String,
        override val iconUrl: String? = null
    ): CurrencyInfo(id, name, symbol, iconUrl)

    data class FiatInfo(
        val code: String,
        override val id: String,
        override val name: String,
        override val symbol: String,
        override val iconUrl: String? = null
    ): CurrencyInfo(id, name, symbol, iconUrl)

    fun matchesQuery(query: String): Boolean {
        val lowerCaseQuery = query.lowercase()
        return name.split(" ").any { it.lowercase().startsWith(lowerCaseQuery) } ||
                symbol.lowercase().startsWith(lowerCaseQuery)
    }

    fun toCurrency(): Currency {
        return when (this) {
            is CryptoInfo -> Currency(
                id = id,
                name = name,
                symbol = symbol,
                type = CurrencyType.CRYPTO,
            )
            is FiatInfo -> Currency(
                id = id,
                name = name,
                symbol = symbol,
                type = CurrencyType.FIAT
            )
        }
    }

}