package com.example.currencysearch2.ui.screens

import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.currencysearch2.domain.InsertViewModel
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun InsertScreen(navController: NavController) {
    val insertViewModel = koinViewModel<InsertViewModel>()
    val insertedText by insertViewModel.insertedText.collectAsState()
    val currentContext = LocalContext.current

    LaunchedEffect(Unit) {
        insertViewModel.toastEvent.collect {
            Toast.makeText(currentContext, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        insertViewModel.insertCompleteEvent.collect {
            navController.popBackStack()
        }
    }

    SharedTransitionLayout {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                //.imePadding()
                //.windowInsetsPadding(WindowInsets.safeContent)
                .padding(bottom = 15.dp)
//            .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back to main page")
                }
                Text(
                    text = "Insert currencies",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            InsertOptions(modifier = Modifier.fillMaxWidth())

            TextField(
                value = insertedText,
                onValueChange = insertViewModel::onTextInserted,
                placeholder = { Text("Insert currencies as JSONArray") },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // This line is causing the weird flashing issue, maybe try shared element again?
                    .skipToLookaheadSize()
            )

            TextButton("Insert", onClick = { insertViewModel.verifyInputCurrencies() }, modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }

}

@Composable
fun InsertOptions(modifier: Modifier) {
    val insertViewModel = koinViewModel<InsertViewModel>()
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            TextButtonConfig("List A", onClick = { insertViewModel.loadJsonStringFromFile("currencyListA.json") }),
            TextButtonConfig("List B", onClick = { insertViewModel.loadJsonStringFromFile("currencyListB.json") }),
            TextButtonConfig("List A & B", onClick = { insertViewModel.loadJsonStringFromFile("combinedCurrencyList.json") }),
            TextButtonConfig("Beautify", onClick = { insertViewModel.beautifyString() }),
            TextButtonConfig("Clear", onClick = { insertViewModel.clearInput() }),
        ).map {
            TextButton(it.title, it.onClick, modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun TextButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall)
    }
}


data class TextButtonConfig(
    val title: String,
    val onClick: () -> Unit,
)