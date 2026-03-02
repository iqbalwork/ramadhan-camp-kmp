package com.iqbalwork.ramadhancamp.feature.home.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShalatJadwal(
    @SerialName("ashar")
    val ashar: String,
    @SerialName("dhuha")
    val dhuha: String,
    @SerialName("dzuhur")
    val dzuhur: String,
    @SerialName("hari")
    val hari: String,
    @SerialName("imsak")
    val imsak: String,
    @SerialName("isya")
    val isya: String,
    @SerialName("maghrib")
    val maghrib: String,
    @SerialName("subuh")
    val subuh: String,
    @SerialName("tanggal")
    val tanggal: Int,
    @SerialName("tanggal_lengkap")
    val tanggalLengkap: String,
    @SerialName("terbit")
    val terbit: String
)