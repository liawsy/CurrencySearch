package com.example.currencysearch.database

import androidx.room.TypeConverter
import com.example.currencysearch.viewmodel.model.CurrencyType

class CurrencyConverter {
    @TypeConverter
    fun fromCurrencyType(type: CurrencyType): String {
        return type.name
    }

    @TypeConverter
    fun toCurrencyType(value: String): CurrencyType {
        return CurrencyType.valueOf(value)
    }
}