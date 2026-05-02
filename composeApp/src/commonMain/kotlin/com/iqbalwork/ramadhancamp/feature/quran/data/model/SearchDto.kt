package com.iqbalwork.ramadhancamp.feature.quran.data.model

import com.iqbalwork.ramadhancamp.feature.home.data.model.surah.SuarahDataDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchRequestDto(
    @SerialName("cari") val cari: String,
    @SerialName("tipe") val tipe: List<String>
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
    @SerialName("data") val data: SuarahDataDto
)
