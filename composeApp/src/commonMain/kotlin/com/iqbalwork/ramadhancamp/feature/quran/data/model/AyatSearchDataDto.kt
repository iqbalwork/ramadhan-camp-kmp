package com.iqbalwork.ramadhancamp.feature.quran.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AyatSearchDataDto(
    @SerialName("id_surat") val idSurat: Int,
    @SerialName("nama_surat") val namaSurat: String,
    @SerialName("nama_surat_arab") val namaSuratArab: String,
    @SerialName("nomor_ayat") val nomorAyat: Int,
    @SerialName("teks_arab") val teksArab: String,
    @SerialName("teks_latin") val teksLatin: String,
    @SerialName("terjemahan_id") val terjemahanId: String
)