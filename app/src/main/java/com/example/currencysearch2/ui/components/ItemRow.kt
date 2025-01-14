package com.example.currencysearch2.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun ItemRow(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 10.dp, horizontal = 15.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold)
        Text(text = subtitle)
    }
}
