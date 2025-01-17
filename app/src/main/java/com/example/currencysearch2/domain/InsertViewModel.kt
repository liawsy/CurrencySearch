package com.example.currencysearch2.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencysearch2.data.CurrencyInfoDTO
import com.example.currencysearch2.data.CurrencyRepository
import com.example.currencysearch2.domain.events.InsertCompleteEvent
import com.example.currencysearch2.domain.events.ToastEvent
import com.example.currencysearch2.domain.model.CurrencyInfo
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


class InsertViewModel(private val context: Context, private val repository: CurrencyRepository): ViewModel() {

    private val _insertedText = MutableStateFlow("")
    val insertedText = _insertedText.asStateFlow()

    private val _toastEvent = MutableSharedFlow<ToastEvent>()
    val toastEvent = _toastEvent.asSharedFlow()

    private val _insertCompleteEvent = MutableSharedFlow<InsertCompleteEvent>()
    val insertCompleteEvent = _insertCompleteEvent.asSharedFlow()

    fun verifyInputCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currencies: List<CurrencyInfoDTO> = Json.decodeFromString(_insertedText.value)
                if (currencies.isEmpty()) {
                    _toastEvent.emit(ToastEvent("Please input a non-empty array!"))
                    return@launch
                }
                insertCurrencies(currencies.map { it.toCurrencyInfo() })
            } catch (e: SerializationException) {
                _toastEvent.emit(ToastEvent("Please input a valid JSON!"))
            } catch (e: IllegalArgumentException) {
                _toastEvent.emit(ToastEvent("Please input the correct data type!"))
            }
        }

    }

    private suspend fun insertCurrencies(currencies: List<CurrencyInfo>) {
        repository.insertCurrencies(currencies)
        _toastEvent.emit(ToastEvent("Insertion to database complete!"))
        _insertCompleteEvent.emit(InsertCompleteEvent)
    }

    fun clearInput() {
        _insertedText.value = ""
    }

    fun loadJsonStringFromFile(fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonString =
                    context.assets.open(fileName).bufferedReader().use { it.readText() }
                _insertedText.value = jsonString
            } catch (e: IOException) {
                _toastEvent.emit(ToastEvent("An error occurred with loading the json"))
            }
        }

    }

    fun onTextInserted(insertedText: String) {
        _insertedText.value = insertedText
    }

    fun beautifyString() {
        viewModelScope.launch {
            try {
                val stringToBeautify = _insertedText.value
                _insertedText.value = stringToBeautify.beautify()
            } catch (e: SerializationException) {
                _toastEvent.emit(ToastEvent("Please enter a valid JSONArray!"))
            }
        }
    }

    private fun String.beautify(): String {
        val stringToJson = Json.parseToJsonElement(this)
        val pretty = Json {
            prettyPrint = true
        }
        return pretty.encodeToString(JsonElement.serializer(), stringToJson)
    }

}