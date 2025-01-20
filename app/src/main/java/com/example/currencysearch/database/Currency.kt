package com.example.currencysearch.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencysearch.viewmodel.model.CurrencyInfo
import com.example.currencysearch.viewmodel.model.CurrencyType

@Entity
data class Currency(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null,
    val type: CurrencyType
) {
    fun toCurrencyInfo(): CurrencyInfo {
        return when (type) {
            CurrencyType.CRYPTO -> CurrencyInfo.CryptoInfo(
                id = id,
                name = name,
                symbol = symbol,
            )
            CurrencyType.FIAT -> CurrencyInfo.FiatInfo(
                id = id,
                name = name,
                symbol = symbol,
                code = code.orEmpty(),
            )
        }
    }
}