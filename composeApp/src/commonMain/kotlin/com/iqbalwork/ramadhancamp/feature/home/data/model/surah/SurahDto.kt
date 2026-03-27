package com.iqbalwork.ramadhancamp.feature.home.data.model.surah


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurahDto(
    @SerialName("code")
    val code: Int,
    @SerialName("data")
    val `data`: List<SuarahDataDto>,
    @SerialName("message")
    val message: String
)