package com.iqbalwork.ramadhancamp.feature.home.data.model.surah


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurahAudioFullDto(
    @SerialName("01")
    val x01: String,
    @SerialName("02")
    val x02: String,
    @SerialName("03")
    val x03: String,
    @SerialName("04")
    val x04: String,
    @SerialName("05")
    val x05: String,
    @SerialName("06")
    val x06: String
)