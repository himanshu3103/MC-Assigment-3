package com.example.matrixcalculator

import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.example.matrixcalculator.ui.theme.MatrixCalculatorTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MatrixCalculatorApp()
        }
    }
}

@Composable
fun MatrixCalculatorApp() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var rowsA by remember { mutableStateOf("2") }
            var colsA by remember { mutableStateOf("2") }
            var rowsB by remember { mutableStateOf("2") }
            var colsB by remember { mutableStateOf("2") }
            var matrixAStr by remember { mutableStateOf("1 2\n3 4") }
            var matrixBStr by remember { mutableStateOf("5 6\n7 8") }
            var operation by remember { mutableStateOf("Add") }
            var result by remember { mutableStateOf("") }

            val matrixOperations = remember { MatrixOperations() }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Matrix Calculator",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Matrix A Dimensions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Matrix A Dimensions:", style = MaterialTheme.typography.bodyLarge)

                    OutlinedTextField(
                        value = rowsA,
                        onValueChange = { rowsA = it },
                        label = { Text("Rows") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(80.dp)
                            .padding(end = 8.dp)
                    )

                    OutlinedTextField(
                        value = colsA,
                        onValueChange = { colsA = it },
                        label = { Text("Cols") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(80.dp)
                    )
                }

                // Matrix A Input
                OutlinedTextField(
                    value = matrixAStr,
                    onValueChange = { matrixAStr = it },
                    label = { Text("Matrix A Elements") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(100.dp),
                )

                // Matrix B Dimensions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Matrix B Dimensions:", style = MaterialTheme.typography.bodyLarge)

                    OutlinedTextField(
                        value = rowsB,
                        onValueChange = { rowsB = it },
                        label = { Text("Rows") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .width(80.dp)
                            .padding(end = 8.dp)
                    )

                    OutlinedTextField(
                        value = colsB,
                        onValueChange = { colsB = it },
                        label = { Text("Cols") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(80.dp)
                    )
                }

                // Matrix B Input
                OutlinedTextField(
                    value = matrixBStr,
                    onValueChange = { matrixBStr = it },
                    label = { Text("Matrix B Elements") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(100.dp),
                )

                // Operation Selection
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Text("Operation:", style = MaterialTheme.typography.bodyLarge)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RadioButton(
                            selected = operation == "Add",
                            onClick = { operation = "Add" }
                        )
                        Text("Add", modifier = Modifier.padding(top = 12.dp))

                        RadioButton(
                            selected = operation == "Subtract",
                            onClick = { operation = "Subtract" }
                        )
                        Text("Subtract", modifier = Modifier.padding(top = 12.dp))

                        RadioButton(
                            selected = operation == "Multiply",
                            onClick = { operation = "Multiply" }
                        )
                        Text("Multiply", modifier = Modifier.padding(top = 12.dp))

                        RadioButton(
                            selected = operation == "Divide",
                            onClick = { operation = "Divide" }
                        )
                        Text("Divide", modifier = Modifier.padding(top = 12.dp))
                    }
                }

                // Calculate Button
                Button(
                    onClick = {
                        try {
                            val rowsAInt = rowsA.toIntOrNull() ?: 2
                            val colsAInt = colsA.toIntOrNull() ?: 2
                            val rowsBInt = rowsB.toIntOrNull() ?: 2
                            val colsBInt = colsB.toIntOrNull() ?: 2

                            result = when (operation) {
                                "Add" -> matrixOperations.addMatrices(
                                    rowsAInt, colsAInt, matrixAStr,
                                    rowsBInt, colsBInt, matrixBStr
                                )
                                "Subtract" -> matrixOperations.subtractMatrices(
                                    rowsAInt, colsAInt, matrixAStr,
                                    rowsBInt, colsBInt, matrixBStr
                                )
                                "Multiply" -> matrixOperations.multiplyMatrices(
                                    rowsAInt, colsAInt, matrixAStr,
                                    rowsBInt, colsBInt, matrixBStr
                                )
                                "Divide" -> matrixOperations.divideMatrices(
                                    rowsAInt, colsAInt, matrixAStr,
                                    rowsBInt, colsBInt, matrixBStr
                                )
                                else -> "Invalid operation"
                            }
                        } catch (e: Exception) {
                            result = "Error: ${e.message}"
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Calculate")
                }

                // Results
                Text("Result:", style = MaterialTheme.typography.bodyLarge)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}