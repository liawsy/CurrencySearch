package com.example.currencysearch2.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currencysearch2.domain.model.CurrencyInfo

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

enum class CurrencyType {
    CRYPTO, FIAT
}
