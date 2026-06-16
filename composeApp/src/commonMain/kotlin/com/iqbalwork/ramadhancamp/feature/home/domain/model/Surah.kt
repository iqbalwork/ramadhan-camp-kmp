package com.iqbalwork.ramadhancamp.feature.home.domain.model

data class Surah(
    val number: Int,
    val arabicName: String,
    val latinName: String,
    val numberOfAyahs: Int,
    val descentPlace: String
)
