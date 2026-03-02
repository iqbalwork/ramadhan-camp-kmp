package com.iqbalwork.ramadhancamp.feature.home.domain.repository

import com.iqbalwork.ramadhancamp.feature.home.domain.model.NextPrayer
import dev.jordond.compass.geolocation.GeolocatorResult
import kotlinx.coroutines.flow.SharedFlow

interface HomeRepository {
    val nextPrayer: SharedFlow<NextPrayer>
    suspend fun getCurrentLocation() : Result<GeolocatorResult>
    suspend fun getShalatSchedule(province: String, city: String): Result<Unit>
    suspend fun observerNextPrayer()
}