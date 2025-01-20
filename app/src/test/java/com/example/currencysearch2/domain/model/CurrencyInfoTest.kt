package com.example.currencysearch2.domain.model

import com.example.currencysearch2.dao.Currency
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CurrencyInfoTest {

    @Test
    fun `matchesQuery should return true when query matches start of name`() {
        val cryptoInfo = CurrencyInfo.CryptoInfo(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC"
        )
        assertTrue(cryptoInfo.matchesQuery("Bit"))
        assertTrue(cryptoInfo.matchesQuery("bitcoin"))
    }

    @Test
    fun `matchesQuery should return true when query matches start of symbol`() {
        val cryptoInfo = CurrencyInfo.CryptoInfo(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC"
        )
        assertTrue(cryptoInfo.matchesQuery("BTC"))
        assertTrue(cryptoInfo.matchesQuery("btc"))
    }

    @Test
    fun `matchesQuery should return false when query does not match name or symbol`() {
        val cryptoInfo = CurrencyInfo.CryptoInfo(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC"
        )
        assertFalse(cryptoInfo.matchesQuery("Eth"))
        assertFalse(cryptoInfo.matchesQuery("ethereum"))
    }

    @Test
    fun `matchesQuery should return true for partial matches at the start of words in name`() {
        val fiatInfo = CurrencyInfo.FiatInfo(
            id = "USD",
            name = "United States Dollar",
            symbol = "$",
            code = "USD"
        )
        assertTrue(fiatInfo.matchesQuery("United"))
        assertTrue(fiatInfo.matchesQuery("States"))
        assertTrue(fiatInfo.matchesQuery("Dollar"))
    }

    @Test
    fun `matchesQuery should return false for partial matches not at the start of words in name`() {
        val fiatInfo = CurrencyInfo.FiatInfo(
            id = "USD",
            name = "United States Dollar",
            symbol = "$",
            code = "USD"
        )
        assertFalse(fiatInfo.matchesQuery("nit")) // Middle of "United"
        assertFalse(fiatInfo.matchesQuery("tes")) // Middle of "States"
    }

    @Test
    fun `matchesQuery should handle case insensitivity`() {
        val cryptoInfo = CurrencyInfo.CryptoInfo(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC"
        )
        assertTrue(cryptoInfo.matchesQuery("bit"))
        assertTrue(cryptoInfo.matchesQuery("BIT"))
    }

    @Test
    fun `matchesQuery should return true for empty query`() {
        val cryptoInfo = CurrencyInfo.CryptoInfo(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC"
        )
        assertTrue(cryptoInfo.matchesQuery(""))
    }

    @Test
    fun `matchesQuery should handle whitespace in query string`() {
        val fiatInfo = CurrencyInfo.FiatInfo(
            id = "USD",
            name = "United States Dollar",
            symbol = "$",
            code = "USD"
        )
        assertFalse(fiatInfo.matchesQuery(" "))
    }

    @Test
    fun `toCurrency should convert CryptoInfo to Currency with type CRYPTO`() {
        val cryptoInfo = CurrencyInfo.CryptoInfo(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC"
        )

        val expectedCurrency = Currency(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC",
            type = CurrencyType.CRYPTO
        )

        assertEquals(expectedCurrency, cryptoInfo.toCurrency())
    }

    @Test
    fun `toCurrency should convert FiatInfo to Currency with type FIAT`() {
        val fiatInfo = CurrencyInfo.FiatInfo(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            code = "USD"
        )

        val expectedCurrency = Currency(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            type = CurrencyType.FIAT
        )

        assertEquals(expectedCurrency, fiatInfo.toCurrency())
    }
}