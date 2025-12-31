package com.example.emergencyalertandroidapplication

import android.app.Application
import com.example.emergencyalertandroidapplication.data.database.EmergencyDatabase
import com.example.emergencyalertandroidapplication.data.remote.RetrofitClient
import com.example.emergencyalertandroidapplication.data.repository.*
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EmergencyApp : Application() {
    val database by lazy { EmergencyDatabase.getDatabase(this) }
    val contactDao by lazy { database.emergencyContactDao() }
    val alertDao by lazy { database.emergencyAlertDao() }
    val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore by lazy { FirebaseFirestore.getInstance() }
    val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    val retrofitClient by lazy { RetrofitClient() }
    val weatherApiService by lazy { retrofitClient.weatherApiService }
    
    // Repositories
    val authRepository by lazy { AuthRepository(firebaseAuth) }
    val contactRepository by lazy { ContactRepository(contactDao, firestore) }
    val alertRepository by lazy { AlertRepository(alertDao, firestore) }
    val locationRepository by lazy { LocationRepository(this, fusedLocationClient) }
    val weatherRepository by lazy { WeatherRepository(weatherApiService) }
}
