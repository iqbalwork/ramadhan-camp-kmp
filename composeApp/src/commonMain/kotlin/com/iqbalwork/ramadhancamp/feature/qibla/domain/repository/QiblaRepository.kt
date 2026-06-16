package com.iqbalwork.ramadhancamp.feature.qibla.domain.repository

import com.iqbalwork.ramadhancamp.feature.qibla.domain.model.QiblaLocation
import dev.jordond.compass.geolocation.GeolocatorResult
import kotlinx.coroutines.flow.Flow

interface QiblaRepository {
    val compassHeading: Flow<Float>
    val isCompassAvailable: Boolean
    suspend fun getQiblaLocation(): Result<QiblaLocation>
    suspend fun checkLocationPermission(): Result<GeolocatorResult>
    fun startCompass()
    fun stopCompass()
}
