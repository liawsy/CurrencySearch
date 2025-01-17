package com.example.currencysearch2

import androidx.room.Room
import com.example.currencysearch2.dao.CurrencyDao
import com.example.currencysearch2.dao.CurrencyDatabase
import com.example.currencysearch2.data.CurrencyRepository
import com.example.currencysearch2.data.CurrencyRepositoryImpl
import com.example.currencysearch2.domain.InsertViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(androidApplication(), CurrencyDatabase::class.java, "currency-search").build()
    }
    single<CurrencyDao> {
        val database = get<CurrencyDatabase>()
        database.currencyDao()
    }
    single<CurrencyRepository> {
        CurrencyRepositoryImpl(get())
    }
    viewModel { InsertViewModel(androidContext(), get()) }
}