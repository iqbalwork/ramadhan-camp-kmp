package com.iqbalwork.ramadhancamp.feature.quran.domain.model

data class Surah(
    val number: Int,
    val namaLatin: String,
    val nama: String,
    val arti: String,
    val jumlahAyat: Int
)

data class Ayat(
    val nomorAyat: Int,
    val teksArab: String,
    val teksLatin: String,
    val teksIndonesia: String,
    val audioUrl: String
)

data class SurahDetail(
    val number: Int,
    val namaLatin: String,
    val nama: String,
    val arti: String,
    val jumlahAyat: Int,
    val ayat: List<Ayat>
)
