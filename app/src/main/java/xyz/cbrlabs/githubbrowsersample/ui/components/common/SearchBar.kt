package xyz.cbrlabs.githubbrowsersample.ui.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    onSearchPressed: (String) -> Unit
) {
    var searchTerm by remember { mutableStateOf(TextFieldValue()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchTerm,
            onValueChange = { searchTerm = it },
            singleLine = true,
            label = { Text("Search Github Repos") },
            textStyle = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .align(Alignment.CenterVertically)
        )
        Button(
            onClick = { onSearchPressed(searchTerm.text) },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        }
    }
}