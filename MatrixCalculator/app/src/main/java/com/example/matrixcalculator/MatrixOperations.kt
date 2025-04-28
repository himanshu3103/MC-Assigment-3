package com.example.matrixcalculator

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

class MatrixOperations {
    companion object {
        init {
            System.loadLibrary("matrix-operations")
        }
    }

    external fun addMatrices(
        rowsA: Int, colsA: Int, matrixA: String,
        rowsB: Int, colsB: Int, matrixB: String
    ): String

    external fun subtractMatrices(
        rowsA: Int, colsA: Int, matrixA: String,
        rowsB: Int, colsB: Int, matrixB: String
    ): String

    external fun multiplyMatrices(
        rowsA: Int, colsA: Int, matrixA: String,
        rowsB: Int, colsB: Int, matrixB: String
    ): String

    external fun divideMatrices(
        rowsA: Int, colsA: Int, matrixA: String,
        rowsB: Int, colsB: Int, matrixB: String
    ): String
}