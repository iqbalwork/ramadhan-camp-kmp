package com.iqbalwork.ramadhancamp.feature.quran.data.datasource

import com.iqbalwork.ramadhancamp.feature.home.data.model.surah.SurahDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.SearchRequestDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.SearchResponseDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.SurahDetailResponseDto
import com.iqbalwork.ramadhancamp.shared.common.network.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.path

class QuranRemoteDatasource(
    private val httpClient: HttpClient
) {
    suspend fun getSurahs(): Result<SurahDto> {
        return httpClient.safeApiCall {
            method = HttpMethod.Get
            url { path("v2/surat") }
        }
    }

    suspend fun getSurahDetail(nomor: Int): Result<SurahDetailResponseDto> {
        return httpClient.safeApiCall {
            method = HttpMethod.Get
            url { path("v2/surat/$nomor") }
        }
    }

    suspend fun search(query: String): Result<SearchResponseDto> {
        return httpClient.safeApiCall {
            method = HttpMethod.Post
            url { path("vector") }
            setBody(SearchRequestDto(
                cari = query,
                tipe = listOf("surat", "ayat"),
                limit = 10,
                minScore = 0.5
            ))
        }
    }
}