package com.example.emergencyalertandroidapplication.data.repository

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.*
import com.example.emergencyalertandroidapplication.data.model.LocationData
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepository(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) {
    suspend fun getCurrentLocation(): Result<LocationData> {
        return try {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(10000)
                .build()

            val location = suspendCancellableCoroutine { continuation ->
                fusedLocationClient.getCurrentLocation(locationRequest, null)
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val address = getAddressFromLocation(location.latitude, location.longitude)
                            continuation.resume(
                                LocationData(
                                    latitude = location.latitude,
                                    longitude = location.longitude,
                                    address = address
                                )
                            )
                        } else {
                            continuation.resumeWithException(Exception("Location is null"))
                        }
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }

            Result.success(location)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0)
        } catch (e: Exception) {
            null
        }
    }
}
