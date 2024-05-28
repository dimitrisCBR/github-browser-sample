package xyz.cbrlabs.githubbrowsersample.ui.components.common

import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CustomButton(text: String, onClick: () -> Unit) {
    FilledTonalButton(onClick = { onClick() }) {
        Text(text)
    }
}