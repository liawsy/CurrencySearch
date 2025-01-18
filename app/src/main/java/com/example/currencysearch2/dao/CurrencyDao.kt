package com.example.currencysearch2.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CurrencyDao {

    @Upsert
    suspend fun upsertCurrencies(currencies: List<Currency>)

    @Query("DELETE FROM currency")
    suspend fun clearAll()

    @Query("SELECT * FROM currency")
    suspend fun getAll(): List<Currency>

}