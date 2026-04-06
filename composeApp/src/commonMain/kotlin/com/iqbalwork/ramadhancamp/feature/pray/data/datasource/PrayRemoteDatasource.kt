package com.iqbalwork.ramadhancamp.feature.pray.data.datasource

import com.iqbalwork.ramadhancamp.feature.home.data.model.shalatSchedule.ShalatScheduleDto
import com.iqbalwork.ramadhancamp.shared.common.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path

class PrayRemoteDatasource(
    private val httpClient: HttpClient
) {
    suspend fun getShalatSchedule(
        province: String,
        city: String,
        month: Int,
        year: Int
    ): Result<ShalatScheduleDto> = httpClient.safeApiCall {
        method = HttpMethod.Post
        url { path("v2/shalat") }
        setBody(
            mapOf(
                "provinsi" to province,
                "kabkota" to city,
                "bulan" to month.toString(),
                "tahun" to year.toString()
            )
        )
    }
}
