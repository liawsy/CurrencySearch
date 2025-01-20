package com.example.currencysearch.repository

import com.example.currencysearch.viewmodel.model.CurrencyInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyInfoDTOTest {

    @Test
    fun `toCurrencyInfo should convert to CryptoInfo when code is null`() {
        val dto = CurrencyInfoDTO(
            id = "BTC",
            name = "Bitcoin",
            symbol = "BTC",
            code = null
        )

        val result = dto.toCurrencyInfo()

        assertEquals(CurrencyInfo.CryptoInfo(id = "BTC", name = "Bitcoin", symbol = "BTC"), result)
    }

    @Test
    fun `toCurrencyInfo should convert to FiatInfo when code is not null`() {
        val dto = CurrencyInfoDTO(
            id = "USD",
            name = "US Dollar",
            symbol = "$",
            code = "USD"
        )

        val result = dto.toCurrencyInfo()

        assertEquals(
            CurrencyInfo.FiatInfo(id = "USD", name = "US Dollar", symbol = "$", code = "USD"),
            result
        )
    }
}