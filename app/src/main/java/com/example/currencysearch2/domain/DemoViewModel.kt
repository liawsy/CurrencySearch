package com.example.currencysearch2.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencysearch2.data.CurrencyInfoDTO
import com.example.currencysearch2.data.CurrencyRepository
import com.example.currencysearch2.data.CurrencySample
import com.example.currencysearch2.domain.events.ButtonEvent
import com.example.currencysearch2.domain.events.InsertCompleteEvent
import com.example.currencysearch2.domain.events.ToastEvent
import com.example.currencysearch2.domain.model.CurrencyInfo
import com.example.currencysearch2.domain.model.DisplayMode
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

class DemoViewModel(private val repository: CurrencyRepository): ViewModel() {

    private val _buttonEvent = MutableSharedFlow<ButtonEvent>(1)
    val buttonEvent = _buttonEvent.asSharedFlow()

    private val _insertedText = MutableStateFlow("")
    val insertedText = _insertedText.asStateFlow()

    private val _toastEvent = MutableSharedFlow<ToastEvent>()
    val toastEvent = _toastEvent.asSharedFlow()

    private val _insertCompleteEvent = MutableSharedFlow<InsertCompleteEvent>()
    val insertCompleteEvent = _insertCompleteEvent.asSharedFlow()

    private val _isTextInputError = MutableStateFlow(false)
    val isTextInputError = _isTextInputError.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _buttonEvent.emit(ButtonEvent.InsertingCurrencies)
            val currencies = repository.getInitialData()
            _buttonEvent.emit(ButtonEvent.InsertCurrenciesComplete(currencies))
        }
    }


    fun toggleDisplayMode(displayMode: DisplayMode) {
        viewModelScope.launch {
            _buttonEvent.emit(ButtonEvent.SwitchDisplayMode(displayMode))
        }
    }

    fun clearCurrencies() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearCurrencies()
            _buttonEvent.emit(ButtonEvent.ClearCurrencies)
        }
    }

    fun tryInsertInputCurrencies() {
        _isTextInputError.value = false
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
                _isTextInputError.value = true
            } catch (e: IllegalArgumentException) {
                _toastEvent.emit(ToastEvent("Please input the correct data type!"))
                _isTextInputError.value = true
            }
        }

    }

    private suspend fun insertCurrencies(currencies: List<CurrencyInfo>) {
        _buttonEvent.emit(ButtonEvent.InsertingCurrencies)
        repository.insertCurrencies(currencies)
        _toastEvent.emit(ToastEvent("Insertion to database complete!"))
        _insertCompleteEvent.emit(InsertCompleteEvent)
        _insertedText.value = ""
        val allCurrencies = repository.getAllCurrencies()
        _buttonEvent.emit(ButtonEvent.InsertCurrenciesComplete(allCurrencies))
    }

    fun clearInput() {
        _isTextInputError.value = false
        _insertedText.value = ""
    }

    fun loadSampleData(dataset: CurrencySample) {
        _isTextInputError.value = false
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _insertedText.value = repository.getRawCurrencyString(dataset)
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