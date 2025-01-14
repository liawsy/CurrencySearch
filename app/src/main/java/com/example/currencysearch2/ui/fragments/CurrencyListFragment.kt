package com.example.currencysearch2.ui.fragments

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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.currencysearch2.domain.SearchViewModel
import com.example.currencysearch2.domain.model.CurrencyInfo
import com.example.currencysearch2.domain.model.CurrencyType
import com.example.currencysearch2.ui.components.ItemRow

class CurrencyListFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

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
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            ListTitle(
                getTitleFromCurrencyTypes(currencyTypes),
                modifier = Modifier.fillMaxWidth()
            )
            SearchBar(modifier = Modifier.fillMaxWidth())
            CurrencyList(currencies, isSearching, modifier = Modifier.fillMaxWidth())
        }
    }

    @Composable
    fun SearchBar(modifier: Modifier = Modifier) {
        val viewModel = viewModel<SearchViewModel>()
        val searchText by viewModel.searchText.collectAsState()
        val isSearching by viewModel.isSearching.collectAsState()
        val keyboardController = LocalSoftwareKeyboardController.current

        TextField(
            leadingIcon = {
                if (isSearching) {
                    IconButton(onClick = { viewModel.cancelSearch() }) {
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
                    IconButton(onClick = { viewModel.cancelSearch() }) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Filled.Cancel,
                            contentDescription = "Cancel search",
                        )
                    }
                }
            },
            value = searchText,
            onValueChange = viewModel::onSearchTextChanged,
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
    fun CurrencyList(currencies: List<CurrencyInfo>, isSearching: Boolean, modifier: Modifier = Modifier) {
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