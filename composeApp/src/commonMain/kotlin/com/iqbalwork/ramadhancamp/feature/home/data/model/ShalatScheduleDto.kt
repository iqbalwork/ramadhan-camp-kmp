package com.iqbalwork.ramadhancamp.feature.home.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShalatScheduleDto(
    @SerialName("code")
    val code: Int,
    @SerialName("data")
    val `data`: ShalatScheduleDataDto,
    @SerialName("message")
    val message: String
)