package com.muss_coding.algovisualizer.presentation.configuration_screen.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Clear
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.muss_coding.algovisualizer.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseAnAlgorithmDropDownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    chooseAnAlgorithmTextFieldValue: String,
    sortingAlgorithms: List<String>,
    onExpandedChanged: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onItemClicked: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Text field for search/input
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = chooseAnAlgorithmTextFieldValue,
            onValueChange = { newValue ->
                if (newValue.isNotEmpty()) {
                    onExpandedChanged()
                }
                onQueryChanged(newValue)
                            },
            label = { Text(stringResource(R.string.choose_an_algorithm)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Sharp.Search,
                    contentDescription = stringResource(R.string.search_for_an_algorithm)
                )
            },
            trailingIcon = {
                Row {
                    if (chooseAnAlgorithmTextFieldValue.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Sharp.Clear,
                            contentDescription = stringResource(R.string.clear_text),
                            modifier = Modifier
                                .clickable {
                                    onQueryChanged("")
                                }
                        )
                    }
                    ExposedDropdownMenuDefaults
                        .TrailingIcon(expanded, modifier = Modifier.clickable { onExpandedChanged() })
                }
            },
            singleLine = true
        )

        // Custom dropdown implementation
        if (expanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                shape = CardDefaults.elevatedShape,
            ) {
                // Filter options based on text field value
                val filteringOptions = sortingAlgorithms.filter {
                    it.contains(
                        chooseAnAlgorithmTextFieldValue,
                        ignoreCase = true
                    )
                }

                Log.d("sortingAlgorithms", sortingAlgorithms.toString())
                val interactionSource = remember {
                    MutableInteractionSource()
                }
                if (filteringOptions.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {

                        items(filteringOptions) { selectionOption ->
                            Text(
                                text = selectionOption,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onItemClicked(selectionOption)
                                        onExpandedChanged()
                                    }
                                    .padding(horizontal = 5.dp)
                                    .hoverable(interactionSource = interactionSource)
                                    .heightIn(min = 48.dp) // For touch targets
                                    .wrapContentSize(Alignment.CenterStart)

                            )
                        }
                    }
                } else {
                    Text(
                        text = "No matching algorithms found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                            .wrapContentSize(Alignment.Center)
                    )
                }
            }
        }
    }
}