package com.iqbalwork.ramadhancamp.feature.home.data.model.shalatSchedule

import kotlinx.serialization.Serializable

@Serializable
data class StringListResponseDto(
    val code: Int,
    val message: String,
    val data: List<String>,
)
