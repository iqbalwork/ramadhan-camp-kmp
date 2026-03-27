package com.iqbalwork.ramadhancamp.feature.home.data.model.surah


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuarahDataDto(
    @SerialName("arti")
    val arti: String,
    @SerialName("audioFull")
    val surahAudioFullDto: SurahAudioFullDto,
    @SerialName("deskripsi")
    val deskripsi: String,
    @SerialName("jumlahAyat")
    val jumlahAyat: Int,
    @SerialName("nama")
    val nama: String,
    @SerialName("namaLatin")
    val namaLatin: String,
    @SerialName("nomor")
    val nomor: Int,
    @SerialName("tempatTurun")
    val tempatTurun: String
)