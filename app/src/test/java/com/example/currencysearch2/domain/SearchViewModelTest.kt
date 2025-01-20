package com.example.currencysearch2.domain

import app.cash.turbine.test
import com.example.currencysearch2.domain.model.CurrencyInfo
import com.example.currencysearch2.domain.model.CurrencyType
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    companion object {
        val currencies = listOf(
            CurrencyInfo.CryptoInfo(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            CurrencyInfo.FiatInfo(id = "USD", name = "US Dollar", symbol = "USD", code = "USD")
        )
    }

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel()
    }

    @Test
    fun `setCurrencies should update currencies`() = runTest {
        viewModel.currencies.test {
            assertEquals(emptyList<CurrencyInfo>(), awaitItem())

            viewModel.setCurrencies(currencies)

            assertEquals(currencies, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setCurrencyTypes should update currency types`() = runTest {
        val initialCurrencyTypes = setOf(CurrencyType.CRYPTO, CurrencyType.FIAT)
        val currencyTypes = listOf(CurrencyType.CRYPTO)

        viewModel.currencyTypes.test {
            assertEquals(initialCurrencyTypes, awaitItem())

            viewModel.setCurrencyTypes(currencyTypes)

            assertEquals(currencyTypes.toSet(), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onSearchTextChanged should update search text and set isSearching to true`() = runTest {
        val searchText = "Bitcoin"

        viewModel.searchText.test {
            viewModel.onSearchTextChanged(searchText)
            assertEquals("", awaitItem())
            assertEquals(searchText, awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        viewModel.isSearching.test {
            assertEquals(true, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `cancelSearch should reset search text and set isSearching to false`() = runTest {
        viewModel.searchText.test {
            assertEquals("", awaitItem())

            viewModel.onSearchTextChanged("Bitcoin")
            assertEquals("Bitcoin", awaitItem())
            
            viewModel.cancelSearch()
            assertEquals("", awaitItem())
            cancelAndConsumeRemainingEvents()
        }

        viewModel.isSearching.test {
            assertEquals(false, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `setIsLoading should update isLoading`() = runTest {
        viewModel.isLoading.test {
            viewModel.setIsLoading(true)
            assertEquals(true, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `currencies flow should filter by accepted types`() = runTest {
        viewModel.setCurrencies(currencies)

        viewModel.currencies.test {
            assertEquals(emptyList<CurrencyInfo>(), awaitItem())

            viewModel.setCurrencyTypes(listOf(CurrencyType.CRYPTO))

            assertEquals(
                listOf(CurrencyInfo.CryptoInfo(id = "BTC", name = "Bitcoin", symbol = "BTC")),
                awaitItem()
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `currencies flow should filter by search text`() = runTest {
        viewModel.setCurrencies(currencies)

        viewModel.currencies.test {
            assertEquals(emptyList<CurrencyInfo>(), awaitItem())

            viewModel.onSearchTextChanged("Bitcoin")

            assertEquals(
                listOf(CurrencyInfo.CryptoInfo(id = "BTC", name = "Bitcoin", symbol = "BTC")),
                awaitItem()
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `currencies flow should return all items when no filters are applied`() = runTest {
        viewModel.currencies.test {
            assertEquals(emptyList<CurrencyInfo>(), awaitItem())

            viewModel.setCurrencies(currencies)

            assertEquals(currencies, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}
