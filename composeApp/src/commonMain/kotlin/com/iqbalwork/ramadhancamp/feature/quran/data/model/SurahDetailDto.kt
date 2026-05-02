package com.iqbalwork.ramadhancamp.feature.quran.data.model

import com.iqbalwork.ramadhancamp.feature.home.data.model.surah.SurahAudioFullDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SurahDetailResponseDto(
    @SerialName("code") val code: Int,
    @SerialName("message") val message: String,
    @SerialName("data") val data: SurahDetailDto
)

@Serializable
data class SurahDetailDto(
    @SerialName("nomor") val nomor: Int,
    @SerialName("nama") val nama: String,
    @SerialName("namaLatin") val namaLatin: String,
    @SerialName("jumlahAyat") val jumlahAyat: Int,
    @SerialName("tempatTurun") val tempatTurun: String,
    @SerialName("arti") val arti: String,
    @SerialName("deskripsi") val deskripsi: String,
    @SerialName("audioFull") val audioFull: SurahAudioFullDto,
    @SerialName("ayat") val ayat: List<AyatDto>? = null
)

@Serializable
data class AyatDto(
    @SerialName("nomorAyat") val nomorAyat: Int,
    @SerialName("teksArab") val teksArab: String,
    @SerialName("teksLatin") val teksLatin: String,
    @SerialName("teksIndonesia") val teksIndonesia: String,
    @SerialName("audio") val audio: SurahAudioFullDto
)
