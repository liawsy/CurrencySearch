package com.example.currencysearch2.data

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyInfoDTO(
    val id: String,
    val name: String,
    val symbol: String,
    val code: String? = null,
)