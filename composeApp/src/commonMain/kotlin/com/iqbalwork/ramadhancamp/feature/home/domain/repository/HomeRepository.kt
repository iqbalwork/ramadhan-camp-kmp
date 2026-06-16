package com.iqbalwork.ramadhancamp.feature.home.domain.repository

import com.iqbalwork.ramadhancamp.feature.home.domain.model.LastSurahRead
import com.iqbalwork.ramadhancamp.feature.home.domain.model.NextPrayer
import com.iqbalwork.ramadhancamp.feature.home.domain.model.Surah
import dev.jordond.compass.Coordinates
import dev.jordond.compass.geolocation.GeolocatorResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val lastSurahRead: Flow<LastSurahRead?>
    val nextPrayer: Flow<NextPrayer>
    suspend fun getCurrentCoordinate() : Result<GeolocatorResult>
    suspend fun getCurrentPlace(coordinates: Coordinates): Result<Triple<String, String, String>>
    suspend fun getShalatSchedule(province: String, city: String): Result<Unit>
    suspend fun getProvinces(): Result<List<String>>
    suspend fun getKabKota(provinsi: String): Result<List<String>>
    suspend fun saveManualLocation(province: String, city: String)
    suspend fun getPopularSurah(): Result<List<Surah>>
    suspend fun saveLastReadSurah(lastSurahRead: LastSurahRead)
}