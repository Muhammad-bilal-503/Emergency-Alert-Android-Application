package com.example.emergencyalertandroidapplication.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.emergencyalertandroidapplication.EmergencyApp
import com.example.emergencyalertandroidapplication.data.model.EmergencyType
import com.example.emergencyalertandroidapplication.ui.viewmodel.HomeViewModel
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToContacts: () -> Unit,
    onNavigateToHistory: () -> Unit,
    app: EmergencyApp = androidx.compose.ui.platform.LocalContext.current.applicationContext as EmergencyApp
) {
    // Request permissions on screen load
    RequestPermissions(onPermissionsGranted = {})
    
    val viewModel = remember { 
        HomeViewModel(
            app.contactRepository,
            app.alertRepository,
            app.locationRepository,
            app.weatherRepository
        )
    }
    val uiState by viewModel.uiState.collectAsState()

    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showEmergencyTypeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emergency Alert") },
                actions = {
                    IconButton(onClick = onNavigateToContacts) {
                        Icon(Icons.Default.Contacts, contentDescription = "Contacts")
                    }
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Weather Card
            uiState.weatherData?.let { weather ->
                WeatherCard(weather = weather)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Emergency Type Selection
            if (uiState.selectedEmergencyType == null) {
                Text(
                    text = "Select Emergency Type",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    EmergencyType.values().forEach { type ->
                        EmergencyTypeChip(
                            type = type,
                            onClick = { viewModel.selectEmergencyType(type) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                SelectedEmergencyTypeCard(
                    type = uiState.selectedEmergencyType,
                    onClear = { viewModel.selectEmergencyType(null) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SOS Button
            if (uiState.countdownSeconds > 0) {
                Text(
                    text = "Alert will be sent in ${uiState.countdownSeconds}...",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    if (uiState.selectedEmergencyType != null) {
                        showConfirmationDialog = true
                    } else {
                        showEmergencyTypeDialog = true
                    }
                },
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                enabled = !uiState.isSendingAlert && uiState.countdownSeconds == 0
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SOS",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Emergency Alert")
                }
            }

            if (uiState.isSendingAlert) {
                CircularProgressIndicator()
                Text("Sending alert...")
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    // Confirmation Dialog
    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            title = { Text("Confirm Emergency Alert") },
            text = {
                Column {
                    Text("Are you sure you want to send an emergency alert?")
                    Text(
                        text = "Type: ${uiState.selectedEmergencyType?.displayName}",
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmationDialog = false
                        viewModel.startCountdown {
                            viewModel.sendEmergencyAlert {
                                // Alert sent successfully
                            }
                        }
                    }
                ) {
                    Text("Send Alert")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Emergency Type Selection Dialog
    if (showEmergencyTypeDialog) {
        AlertDialog(
            onDismissRequest = { showEmergencyTypeDialog = false },
            title = { Text("Select Emergency Type") },
            text = {
                Column {
                    EmergencyType.values().forEach { type ->
                        ListItem(
                            headlineContent = { Text(type.displayName) },
                            leadingContent = { Text(type.emoji, fontSize = 24.sp) },
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.selectEmergencyType(type)
                                showEmergencyTypeDialog = false
                            }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showEmergencyTypeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun WeatherCard(weather: com.example.emergencyalertandroidapplication.data.model.WeatherData) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${weather.temperature.toInt()}°C",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = weather.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Humidity: ${weather.humidity}%",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Wind: ${weather.windSpeed} m/s",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun EmergencyTypeChip(
    type: EmergencyType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = false,
        onClick = onClick,
        label = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Text(type.emoji, fontSize = 24.sp)
                Text(type.displayName, style = MaterialTheme.typography.labelSmall)
            }
        },
        modifier = modifier
    )
}

@Composable
fun SelectedEmergencyTypeCard(
    type: EmergencyType,
    onClear: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(type.emoji, fontSize = 32.sp)
                Column {
                    Text(
                        text = "Selected: ${type.displayName}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Ready to send alert",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onClear) {
                Icon(Icons.Default.Close, contentDescription = "Clear")
            }
        }
    }
}
