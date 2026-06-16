package com.iqbalwork.ramadhancamp.feature.quran.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SearchRequestDto(
    @SerialName("cari") val cari: String,
    @SerialName("tipe") val tipe: List<String>,
    @SerialName("limit") val limit: Int = 10,
    @SerialName("minScore") val minScore: Double = 0.5
)

@Serializable
data class SearchResponseDto(
    @SerialName("status") val status: String,
    @SerialName("cari") val cari: String,
    @SerialName("jumlah") val jumlah: Int,
    @SerialName("hasil") val hasil: List<SearchResultDto>
)

@Serializable
data class SearchResultDto(
    @SerialName("tipe") val tipe: String,
    @SerialName("skor") val skor: Double,
    @SerialName("data") val data: JsonElement
)