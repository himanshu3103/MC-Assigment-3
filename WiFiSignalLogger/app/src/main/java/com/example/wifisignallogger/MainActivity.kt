package com.example.wifisignallogger

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.material3.ExperimentalMaterial3Api


data class WiFiMeasurement(
    val bssid: String,
    val ssid: String,
    val signalStrengths: List<Int>
)

data class LocationData(
    val name: String,
    val wifiMeasurement: WiFiMeasurement
)

@OptIn(ExperimentalMaterial3Api::class)


class MainActivity : ComponentActivity() {
    private lateinit var wifiManager: WifiManager
    private val locationDataList = mutableStateListOf<LocationData>()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.entries.all { it.value }
        if (allGranted) {
            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager

        // Request permissions
        val requiredPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE
        )

        requestPermissionLauncher.launch(requiredPermissions)

        // Generate initial sample data with 3 locations
        generateSampleData()

        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }

    private fun generateSampleData() {
        val locations = listOf("Living Room", "Kitchen", "Bedroom")

        locations.forEach { location ->
            val bssid = generateRandomBSSID()
            val ssid = "Sample-WiFi-${Random.nextInt(1, 10)}"
            val signalStrengths = List(100) { Random.nextInt(-90, -30) }

            locationDataList.add(
                LocationData(
                    name = location,
                    wifiMeasurement = WiFiMeasurement(
                        bssid = bssid,
                        ssid = ssid,
                        signalStrengths = signalStrengths
                    )
                )
            )
        }
    }

    private fun generateRandomBSSID(): String {
        return (1..6).joinToString(":") {
            Random.nextInt(0, 256).toString(16).padStart(2, '0').uppercase()
        }
    }

    @Composable
    fun MainScreen() {
        val showAddDialog = remember { mutableStateOf(false) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("WiFi Signal Strength Logger") },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddDialog.value = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Add, "Add new location")
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(locationDataList) { locationData ->
                    LocationCard(
                        locationData = locationData,
                        onDelete = { locationDataList.remove(locationData) },
                        onRefresh = {
                            scope.launch {
                                refreshLocationData(locationData.name, context)
                            }
                        }
                    )
                }
            }

            if (showAddDialog.value) {
                AddLocationDialog(
                    onDismiss = { showAddDialog.value = false },
                    onAdd = { locationName ->
                        scope.launch {
                            collectNewLocationData(locationName, context)
                            showAddDialog.value = false
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun LocationCard(
        locationData: LocationData,
        onDelete: () -> Unit,
        onRefresh: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = locationData.name,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.titleLarge
                    )

                    Row {
                        IconButton(onClick = onRefresh) {
                            Icon(Icons.Filled.Refresh, "Refresh")
                        }

                        IconButton(onClick = onDelete) {
                            Icon(Icons.Filled.Delete, "Delete")
                        }
                    }
                }

                Text(
                    text = "SSID: ${locationData.wifiMeasurement.ssid}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "BSSID: ${locationData.wifiMeasurement.bssid}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                SignalStrengthGraph(
                    signalStrengths = locationData.wifiMeasurement.signalStrengths
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Min: ${locationData.wifiMeasurement.signalStrengths.minOrNull() ?: 0} dBm")
                    Text("Max: ${locationData.wifiMeasurement.signalStrengths.maxOrNull() ?: 0} dBm")
                    Text("Avg: ${locationData.wifiMeasurement.signalStrengths.average().toInt()} dBm")
                }
            }
        }
    }

    @Composable
    fun SignalStrengthGraph(signalStrengths: List<Int>) {
        val minSignal = -90 // Typical minimum WiFi signal
        val maxSignal = -30 // Typical maximum WiFi signal

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF5F5F5))
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val width = size.width
                val height = size.height
                val segmentWidth = width / (signalStrengths.size - 1)

                // Draw grid lines
                val paint = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f)

                for (i in 0..4) {
                    val y = height - (height * i / 4)
                    drawLine(
                        color = Color.LightGray,
                        start = androidx.compose.ui.geometry.Offset(0f, y),
                        end = androidx.compose.ui.geometry.Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                // Draw signal strength line
                for (i in 0 until signalStrengths.size - 1) {
                    val startX = i * segmentWidth
                    val endX = (i + 1) * segmentWidth

                    val normalizedStartSignal = (signalStrengths[i] - minSignal) / (maxSignal - minSignal).toFloat()
                    val normalizedEndSignal = (signalStrengths[i + 1] - minSignal) / (maxSignal - minSignal).toFloat()

                    val startY = height - (normalizedStartSignal * height)
                    val endY = height - (normalizedEndSignal * height)

                    drawLine(
                        color = Color.Blue,
                        start = androidx.compose.ui.geometry.Offset(startX, startY),
                        end = androidx.compose.ui.geometry.Offset(endX, endY),
                        strokeWidth = 3f
                    )
                }
            }

            // Add axis labels
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                Text(
                    text = "$maxSignal dBm",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$minSignal dBm",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }

    @Composable
    fun AddLocationDialog(
        onDismiss: () -> Unit,
        onAdd: (String) -> Unit
    ) {
        var locationName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Add New Location") },
            text = {
                Column {
                    Text("Enter location name:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = locationName,
                        onValueChange = { locationName = it },
                        label = { Text("Location Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (locationName.isNotBlank()) {
                            onAdd(locationName)
                        }
                    },
                    enabled = locationName.isNotBlank()
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }

    private suspend fun collectNewLocationData(locationName: String, context: Context) {
        try {
            if (!wifiManager.isWifiEnabled) {
                showToast(context, "WiFi is disabled. Please enable it.")
                return
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                showToast(context, "Location permission required")
                return
            }

            val strongestAP = getStrongestAP(context)
            if (strongestAP == null) {
                showToast(context, "No WiFi networks found")
                return
            }

            showToast(context, "Logging signals for ${strongestAP.second}...")

            val signalStrengths = mutableListOf<Int>()

            // Collect 100 measurements
            repeat(100) {
                wifiManager.startScan()
                delay(100) // Small delay to get new readings

                val scanResults = wifiManager.scanResults
                val ap = scanResults.find { it.BSSID == strongestAP.first }

                if (ap != null) {
                    signalStrengths.add(ap.level)
                } else {
                    signalStrengths.add(-80) // Default value if AP not found
                }

                delay(100) // Space out the readings
            }

            // Add the new location data
            locationDataList.add(
                LocationData(
                    name = locationName,
                    wifiMeasurement = WiFiMeasurement(
                        bssid = strongestAP.first,
                        ssid = strongestAP.second,
                        signalStrengths = signalStrengths
                    )
                )
            )

            showToast(context, "Location data collected successfully")

        } catch (e: Exception) {
            showToast(context, "Error: ${e.message}")
        }
    }

    private suspend fun refreshLocationData(locationName: String, context: Context) {
        try {
            val index = locationDataList.indexOfFirst { it.name == locationName }
            if (index == -1) return

            if (!wifiManager.isWifiEnabled) {
                showToast(context, "WiFi is disabled. Please enable it.")
                return
            }

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
                showToast(context, "Location permission required")
                return
            }

            val strongestAP = getStrongestAP(context)
            if (strongestAP == null) {
                showToast(context, "No WiFi networks found")
                return
            }

            showToast(context, "Refreshing signals for ${strongestAP.second}...")

            val signalStrengths = mutableListOf<Int>()

            // Collect 100 measurements
            repeat(100) {
                wifiManager.startScan()
                delay(100) // Small delay to get new readings

                val scanResults = wifiManager.scanResults
                val ap = scanResults.find { it.BSSID == strongestAP.first }

                if (ap != null) {
                    signalStrengths.add(ap.level)
                } else {
                    signalStrengths.add(-80) // Default value if AP not found
                }

                delay(100) // Space out the readings
            }

            // Update the location data
            locationDataList[index] = LocationData(
                name = locationName,
                wifiMeasurement = WiFiMeasurement(
                    bssid = strongestAP.first,
                    ssid = strongestAP.second,
                    signalStrengths = signalStrengths
                )
            )

            showToast(context, "Location data refreshed successfully")

        } catch (e: Exception) {
            showToast(context, "Error: ${e.message}")
        }
    }

    private fun getStrongestAP(context: Context): Pair<String, String>? {
        if (!wifiManager.isWifiEnabled) {
            return null
        }

        wifiManager.startScan()
        val scanResults = wifiManager.scanResults

        if (scanResults.isEmpty()) {
            return null
        }

        // Find the strongest AP
        val strongestAP = scanResults.maxByOrNull { it.level }

        return if (strongestAP != null) {
            Pair(strongestAP.BSSID, strongestAP.SSID)
        } else {
            null
        }
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}