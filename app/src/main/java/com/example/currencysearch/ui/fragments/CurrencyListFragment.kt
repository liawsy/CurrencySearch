package com.example.currencysearch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.compose.content
import com.example.currencysearch.viewmodel.SearchViewModel
import com.example.currencysearch.viewmodel.model.CurrencyInfo
import com.example.currencysearch.viewmodel.model.CurrencyType
import com.example.currencysearch.ui.components.ItemRow
import com.example.currencysearch.ui.components.ShimmerLoaderItem
import kotlinx.coroutines.launch

class CurrencyListFragment : Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = content {
        CurrencyListComponent()
    }

    fun setDisplayCurrencies(currencyTypes: List<CurrencyType>) {
        searchViewModel.setCurrencyTypes(currencyTypes)
    }

    fun updateCurrencies(currencies: List<CurrencyInfo>) {
        searchViewModel.setCurrencies(currencies)
    }

    fun setIsLoading(isLoading: Boolean) {
        searchViewModel.setIsLoading(isLoading)
    }

    private fun getTitleFromCurrencyTypes(currencyTypes: Set<CurrencyType>): String {
        val types = currencyTypes.toList()
        return when (types.size) {
            1 -> types.first().currencyName
            else -> "All currencies"
        }
    }

    @Composable
    fun CurrencyListComponent() {
        val currencyTypes by searchViewModel.currencyTypes.collectAsState()
        val currencies by searchViewModel.currencies.collectAsState()
        val isSearching by searchViewModel.isSearching.collectAsState()
        val isLoading by searchViewModel.isLoading.collectAsState()
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            ListTitle(
                getTitleFromCurrencyTypes(currencyTypes),
                modifier = Modifier.fillMaxWidth()
            )

            if (isLoading) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(10) {
                        ShimmerLoaderItem(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp))
                    }
                }
            } else {
                SearchBar(modifier = Modifier.fillMaxWidth())
                CurrencyList(currencies, isSearching, currencyTypes, modifier = Modifier.fillMaxWidth())
            }

        }
    }

    @Composable
    fun SearchBar(modifier: Modifier = Modifier) {
        val searchText by searchViewModel.searchText.collectAsState()
        val isSearching by searchViewModel.isSearching.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            leadingIcon = {
                if (isSearching) {
                    IconButton(onClick = { searchViewModel.cancelSearch() }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Return to original content",
                        )
                    }
                } else {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                }
            },
            trailingIcon = {
                if (isSearching) {
                    IconButton(onClick = { searchViewModel.cancelSearch() }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel search",
                        )
                    }
                }
            },
            value = searchText,
            onValueChange = searchViewModel::onSearchTextChanged,
            placeholder = { Text("Search") },
            shape = RoundedCornerShape(100),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
            modifier = modifier
        )
    }

    @Composable
    fun ListTitle(title: String, modifier: Modifier = Modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = modifier
        )
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    fun CurrencyList(currencies: List<CurrencyInfo>, isSearching: Boolean, currencyTypes: Set<CurrencyType>, modifier: Modifier = Modifier) {
        SharedTransitionLayout {
            AnimatedVisibility(
                visible = currencies.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                EmptyComponent(isSearching = isSearching, modifier = modifier)
            }
            AnimatedVisibility(
                visible = currencies.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                val coroutineScope = rememberCoroutineScope()
                val listState = rememberLazyListState()

                LaunchedEffect(currencyTypes) {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0) // Scroll to the first item
                    }
                }

                LazyColumn(
                    modifier = modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .skipToLookaheadSize()
                        .animateContentSize(
                            animationSpec = spring()
                        ),
                    contentPadding = PaddingValues(vertical = 5.dp),
                    state = listState,
                ) {
                    items(currencies, key = { it.id }) { currency ->
                        ItemRow(
                            title = currency.name,
                            subtitle = currency.symbol,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }

        }
    }

    @Composable
    fun EmptyComponent(isSearching: Boolean, modifier: Modifier = Modifier) {
        Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("(;-;)", fontWeight = FontWeight.Bold)
            Text(if (isSearching) "No results found" else "Nothing here to see!")
        }
    }

}