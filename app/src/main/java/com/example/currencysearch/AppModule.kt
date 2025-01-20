package com.example.currencysearch

import androidx.room.Room
import com.example.currencysearch.database.CurrencyDao
import com.example.currencysearch.database.CurrencyDatabase
import com.example.currencysearch.repository.CurrencyRepository
import com.example.currencysearch.repository.CurrencyRepositoryImpl
import com.example.currencysearch.viewmodel.DemoViewModel
import kotlinx.coroutines.Dispatchers
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
        CurrencyRepositoryImpl(androidContext(), get())
    }
    viewModel { DemoViewModel(get(), Dispatchers.IO) }
}