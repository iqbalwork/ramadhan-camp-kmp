package com.iqbalwork.ramadhancamp.feature.home.domain.model

data class LastSurahRead(
    val surahId: Int,
    val surahName: String,
    val ayatNumber: Int,
    val readDate: String
)
