package com.example.currencysearch2.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtonList(actionButtonData: List<ActionButtonInfo>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(12.dp)
        )
    ) {
        actionButtonData.map { ActionButton(it, modifier = Modifier.weight(1f)) }
    }
}

@Composable
fun ActionButton(buttonInfo: ActionButtonInfo, modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(10.dp)) {
        IconButton(onClick = buttonInfo.onClick) {
            Icon(
                buttonInfo.icon,
                contentDescription = buttonInfo.text,
                modifier = modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(100)
                    )
                    .padding(10.dp)
            )
        }
        Text(text = buttonInfo.text, style = MaterialTheme.typography.labelSmall)
    }
}

data class ActionButtonInfo(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit
)