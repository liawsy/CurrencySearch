package com.example.currencysearch2.dao

import androidx.room.TypeConverter

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