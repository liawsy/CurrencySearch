package com.example.currencysearch2.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Currency(
    @PrimaryKey
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null,
    val type: CurrencyType
)

enum class CurrencyType {
    CRYPTO, FIAT
}
