# WiFi Signal Strength Logger

A Jetpack Compose Android application that logs and visualizes WiFi access point signal strength across different locations.

## Features

- Track WiFi signal strength (RSS) at different locations
- Monitor the strongest access point for each location
- Collect 100 signal strength measurements per location
- Visualize signal strength variations with line graphs
- Compare signal patterns across different locations
- Add new locations with real-time WiFi scanning
- Refresh existing location data
- View detailed statistics (min/max/average signal strength)

## Screenshots

[Screenshots will be added here]

## Requirements

- Android 7.0 (API level 24) or higher
- WiFi enabled device
- Location services enabled

## Permissions

This app requires the following permissions:
- `ACCESS_FINE_LOCATION`: To scan for WiFi access points
- `ACCESS_WIFI_STATE`: To access WiFi information
- `CHANGE_WIFI_STATE`: To initiate WiFi scans

## How to Use

1. **Launch the app**
   - The app initializes with three sample locations showing randomized data

2. **View location details**
   - Each location card shows:
     - Location name
     - SSID and BSSID of the strongest access point
     - Signal strength graph (100 measurements)
     - Min, max, and average signal strength values

3. **Add a new location**
   - Tap the "+" floating action button
   - Enter a location name
   - The app will collect 100 real-time measurements from the strongest WiFi AP
   - A new location card will be added to the list

4. **Refresh location data**
   - Tap the refresh icon on any location card
   - The app will collect new measurements for that location

5. **Delete a location**
   - Tap the delete icon on any location card
   - The location will be removed from the list

## Technical Details

- Built with Kotlin and Jetpack Compose
- Uses Android's WifiManager to scan for access points
- Implements runtime permission handling for location access
- Visualizes signal strength using Compose Canvas

## Understanding the Data

- Signal strength is measured in dBm (decibels relative to one milliwatt)
- Typically ranges from -90 dBm (very weak) to -30 dBm (very strong)
- The higher the value (closer to 0), the stronger the signal
- Signal strength can vary due to:
  - Distance from the access point
  - Physical obstacles
  - Interference from other devices
  - Access point transmission power

## Troubleshooting

If you encounter issues with the app:

1. **No WiFi networks found**
   - Ensure WiFi is enabled on your device
   - Check that location services are enabled

2. **Permission denied errors**
   - Grant all requested permissions in your device settings

3. **Scan failures**
   - Some devices limit the frequency of WiFi scans
   - Wait a few seconds between scans

## Development

### Project Structure

- `MainActivity.kt`: Contains all the app logic and UI components
- `data classes`: 
  - `WiFiMeasurement`: Stores signal strength data for a specific AP
  - `LocationData`: Associates a location name with WiFi measurements

### Key Components

- **Permission Handling**: Runtime permission requests for location access
- **WiFi Scanning**: Uses WifiManager to scan for available networks
- **Data Collection**: Measures signal strength 100 times per location
- **UI**: Built with Jetpack Compose Material3 components
- **Visualization**: Custom graph implementation using Compose Canvas

## Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync with Gradle files
4. Build and run on an Android device or emulator

## Future Improvements

- Export data to CSV/JSON for analysis
- Compare multiple access points at the same location
- Implement a map view to visualize signal strength geographically
- Add heatmap visualization options
- Support for recording signal strength over longer periods
- Ability to categorize and filter locations
- Dark mode support
