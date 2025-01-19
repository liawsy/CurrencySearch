package com.example.currencysearch2.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun DismissibleTextBox(
    text: String,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null
) {
    var isDismissed by rememberSaveable { mutableStateOf(false) }
    AnimatedVisibility(
        visible = !isDismissed,
    ) {
        Box(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(6.dp)
            )
        ) {
            IconButton(
                onClick = { isDismissed = true },
                modifier = Modifier
                    .padding(top = 8.dp, end = 8.dp)
                    .size(18.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = null)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 15.dp,
                    bottom = 15.dp,
                    end = 26.dp
                )
            ) {
                trailingIcon?.let {
                    Icon(it, contentDescription = null)
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }
    }
}