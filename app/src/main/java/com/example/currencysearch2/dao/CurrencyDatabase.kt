package com.example.currencysearch2.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Currency::class], version = 1)
@TypeConverters(CurrencyConverter::class)
abstract class CurrencyDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}