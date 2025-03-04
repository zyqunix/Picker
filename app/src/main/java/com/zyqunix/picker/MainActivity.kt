package com.zyqunix.picker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zyqunix.picker.ui.theme.PickerTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("Add Players") }
    var inputText by remember { mutableStateOf("") }
    var history by remember { mutableStateOf(listOf<String>()) }

    val radioOptions = listOf("Players", "Numbers", "Items")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }


    fun isValidPositiveInteger(value: String): Boolean {
        return value.toIntOrNull()?.let { it > 1 } == true
    }

    fun pickRandom(max: Int): Int {
        return Random.nextInt(1, max + 1)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            radioOptions.forEach { text ->
                Column (
                    Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                            }
                        )
                        .padding(horizontal = 10.dp)
                        .align(Alignment.TopCenter)
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }

            Text(
                text = buildAnnotatedString {
                    if (text == "Add Players") {
                        text = "Add Players"
                    } else {
                        append("Player ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text.removePrefix("Player ").removeSuffix(" was chosen!")) }
                        append(" was chosen!")
                    }

                },
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = inputText,
                onValueChange = {
                    if (it.isEmpty() || isValidPositiveInteger(it)) {
                        inputText = it
                    }
                },
                label = { Text("Enter a number of players") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (isValidPositiveInteger(inputText)) {
                    val randomPlayer = pickRandom(inputText.toInt())
                    text = "Player $randomPlayer was chosen!"
                    history = history + "Player $randomPlayer"
                } else {
                    text = "Please add more than 1 Player"
                }
            }) {
                Text(text = "Pick Random Player")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (history.isNotEmpty()) {
                Text("History", style = MaterialTheme.typography.titleMedium)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                    ) {
                        items(history) { item ->
                            Text(text = item, modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PickerTheme {
        MainScreen()
    }
}
