package com.example.currencysearch2.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencysearch2.domain.events.ToastEvent
import java.io.IOException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * TODO: Pass in application context via dependency injection.
 */
class InsertViewModel: ViewModel() {

    private val _insertedText = MutableStateFlow("")
    val insertedText = _insertedText.asStateFlow()

    private val _toastEvent = MutableSharedFlow<ToastEvent>()
    val toastEvent = _toastEvent.asSharedFlow()

    fun verifyInputCurrencies() {
        // Convert currency info to a list
        // Alternatively, pass it back to SearchViewModel?
        // When verified, post a success event, which will notify the InsertCurrenciesScreen
        // InsertCurrenciesScreen passes the currencies to DemoViewModel which then inserts to the DB.
        // Or we can pass
    }

//    fun loadJsonStringFromFile(fileName: String) {
//        try {
//            val assetsManager = context.assets
//            val file = assetsManager.open(fileName)
//        } catch (e: IOException) {
//
//        }
//
//    }

    fun onTextInserted(insertedText: String) {
        _insertedText.value = insertedText
    }

    fun beautifyString() {
        viewModelScope.launch {
            try {
                val stringToJson = Json.parseToJsonElement(_insertedText.value)
                val pretty = Json {
                    prettyPrint = true
                }
                _insertedText.value = pretty.encodeToString(JsonElement.serializer(), stringToJson)
            } catch (e: SerializationException) {
                _toastEvent.emit(ToastEvent("Please enter a valid JSONArray!"))
            }
        }


    }

}