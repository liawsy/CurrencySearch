package com.example.currencysearch2.domain

import app.cash.turbine.test
import com.example.currencysearch2.data.CurrencyRepository
import com.example.currencysearch2.data.CurrencySample
import com.example.currencysearch2.domain.events.ButtonEvent
import com.example.currencysearch2.domain.events.ToastEvent
import com.example.currencysearch2.domain.model.CurrencyInfo
import com.example.currencysearch2.domain.model.DisplayMode
import com.example.currencysearch2.stubs.CurrencyRepositoryStub
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DemoViewModelTest {

    private lateinit var viewModel: DemoViewModel
    private lateinit var repository: CurrencyRepository
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        repository = CurrencyRepositoryStub()
        viewModel = DemoViewModel(repository, testDispatcher)
    }

    @Test
    fun `toggleDisplayMode should emit SwitchDisplayMode`() = runTest {
        val displayMode = DisplayMode.ALL_CURRENCIES
        viewModel.toggleDisplayMode(displayMode)
        viewModel.buttonEvent.test {
            assertEquals(ButtonEvent.SwitchDisplayMode(displayMode), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `clearCurrencies should emit ClearCurrencies`() = runTest {
        viewModel.clearCurrencies()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.buttonEvent.test {
            assertEquals(ButtonEvent.ClearCurrencies, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `tryInsertInputCurrencies should reset error state and insert valid array to repository`() =
        runTest {
            val validJson = """[{"id":"BTC","name":"Bitcoin","symbol":"BTC"}]"""
            val currencies =
                listOf(CurrencyInfo.CryptoInfo(id = "BTC", name = "Bitcoin", symbol = "BTC"))
            viewModel.onTextInserted(validJson)
            testDispatcher.scheduler.advanceUntilIdle()

            val job = launch {
                viewModel.tryInsertInputCurrencies()
                testDispatcher.scheduler.advanceUntilIdle()
            }

            viewModel.buttonEvent.test {
                // Replace cache event will be emitted.
                assertEquals(ButtonEvent.InsertCurrenciesComplete(emptyList()), awaitItem())
                // Actual event to verify
                assertEquals(ButtonEvent.InsertingCurrencies, awaitItem())
                assertEquals(ButtonEvent.InsertCurrenciesComplete(currencies), awaitItem())
                cancelAndConsumeRemainingEvents()
            }

            assertFalse(viewModel.isTextInputError.value)
            assertEquals("", viewModel.insertedText.value)

            job.join()
            job.cancel()
        }

    @Test
    fun `tryInsertInputCurrencies should emit toastEvent and set error state to true with an empty array`() =
        runTest {
            val emptyJson = "[]"
            viewModel.onTextInserted(emptyJson)

            viewModel.toastEvent.test {
                viewModel.tryInsertInputCurrencies()
                testDispatcher.scheduler.advanceUntilIdle()

                assertEquals(ToastEvent("Please input a non-empty array!"), awaitItem())
                assertTrue(viewModel.isTextInputError.value)
                cancelAndConsumeRemainingEvents()
            }
        }


    @Test
    fun `tryInsertInputCurrencies should emit toast and set error state to true for SerializationException`() = runTest {
        val invalidJson = "invalid"
        viewModel.onTextInserted(invalidJson)

        viewModel.toastEvent.test {
            viewModel.tryInsertInputCurrencies()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(ToastEvent("Please input a valid JSON!"), awaitItem())
            assertTrue(viewModel.isTextInputError.value)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `clearInput should reset error and text state`() = runTest {
        viewModel.clearCurrencies()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals("", viewModel.insertedText.value)
        assertFalse(viewModel.isTextInputError.value)
    }

    @Test
    fun `onTextInserted should update inserted text`() = runTest {
        val testText = "Inserted text"
        viewModel.onTextInserted(testText)
        assertEquals(testText, viewModel.insertedText.value)
    }

    @Test
    fun `loadSampleData should update reset error and update inserted text`() = runTest {
        viewModel.loadSampleData(CurrencySample.BOTH)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(CurrencyRepositoryStub.CURRENCY_STRING, viewModel.insertedText.value)
    }

    @Test
    fun `beautifyString should update inserted text with indentation for valid JSON array`() = runTest {
        val validJson = """[{"id":"BTC","name":"Bitcoin","symbol":"BTC"}]"""
        val beautifiedString = """
            [
                {
                    "id": "BTC",
                    "name": "Bitcoin",
                    "symbol": "BTC"
                }
            ]
        """.trimIndent()
        viewModel.onTextInserted(validJson)
        viewModel.insertedText.test {
            viewModel.beautifyString()
            testDispatcher.scheduler.advanceUntilIdle()

            // Initial text value before beautifying
            assertEquals(validJson, awaitItem())
            // Verify beautified string
            assertEquals(beautifiedString, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `beautifyString should emit toast event for invalid JSON array`() = runTest {
        val invalidJson = "{ invalid"
        viewModel.onTextInserted(invalidJson)

        viewModel.toastEvent.test {
            viewModel.beautifyString()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(ToastEvent("Please enter a valid JSONArray!"), awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

}