package com.example.currencysearch.ui.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.currencysearch.repository.CurrencySample
import com.example.currencysearch.viewmodel.DemoViewModel
import com.example.currencysearch.ui.components.DismissibleTextBox
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun InsertScreen(navController: NavController) {
    val insertViewModel = koinViewModel<DemoViewModel>(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
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

    BackHandler {
        insertViewModel.clearInput()
        navController.popBackStack()
    }

    SharedTransitionLayout {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        insertViewModel.clearInput()
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back to main page"
                    )
                }
                Text(
                    text = "Insert currencies",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            DismissibleTextBox(
                trailingIcon = Icons.Filled.EditNote,
                text = "If a code field is present, the currency is assumed to be fiat. If a currency of the same ID exists in the database, it will be replaced with the latest entry.",
                modifier = Modifier.fillMaxWidth()
            )

            InsertOptions(
                modifier = Modifier
                    .fillMaxWidth()
                    .skipToLookaheadSize()
                    .animateContentSize()
            )

            InsertTextField(
                modifier = Modifier
                    .weight(1f)
                    .skipToLookaheadSize()
                    .animateContentSize()
            )

            TextButton(
                "Insert",
                onClick = { insertViewModel.tryInsertInputCurrencies() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

    }
}

@Composable
fun InsertTextField(modifier: Modifier = Modifier) {
    val insertViewModel = koinViewModel<DemoViewModel>(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    val insertedText by insertViewModel.insertedText.collectAsState()
    val isError by insertViewModel.isTextInputError.collectAsState()
    val errorMsg =
        "Insert your currencies in a valid JSON Array format. Each currency must have an ID, name and symbol."

    TextField(
        value = insertedText,
        onValueChange = insertViewModel::onTextInserted,
        placeholder = { Text("Insert currencies as JSONArray") },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        supportingText = {
            if (isError) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size((MaterialTheme.typography.labelSmall.fontSize.value * 1.5).dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Justify,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

            }

        },
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun InsertOptions(modifier: Modifier = Modifier) {
    val insertViewModel = koinViewModel<DemoViewModel>(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    )
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(
            TextButtonConfig(
                "List A",
                onClick = { insertViewModel.loadSampleData(CurrencySample.DATASET_A) }),
            TextButtonConfig(
                "List B",
                onClick = { insertViewModel.loadSampleData(CurrencySample.DATASET_B) }),
            TextButtonConfig(
                "List A & B",
                onClick = { insertViewModel.loadSampleData(CurrencySample.BOTH) }),
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