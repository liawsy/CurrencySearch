package com.example.currencysearch.repository

import com.example.currencysearch.viewmodel.model.CurrencyInfo
import kotlinx.serialization.Serializable

@Serializable
data class CurrencyInfoDTO(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null,
) {

    fun toCurrencyInfo(): CurrencyInfo {
        return if (code == null) {
            CurrencyInfo.CryptoInfo(
                id = id,
                name = name,
                symbol = symbol,
            )
        } else {
            CurrencyInfo.FiatInfo(
                id = id,
                name = name,
                symbol = symbol,
                code = code,
            )
        }
    }

 }