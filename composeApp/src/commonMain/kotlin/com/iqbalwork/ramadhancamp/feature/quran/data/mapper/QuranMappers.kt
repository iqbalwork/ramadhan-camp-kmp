package com.iqbalwork.ramadhancamp.feature.quran.data.mapper

import com.iqbalwork.ramadhancamp.feature.home.data.model.surah.SuarahDataDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.AyatDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.AyatSearchDataDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.SearchResultDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.SurahDetailDto
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Ayat
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.AyatSearchResult
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SearchResult
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Surah
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SurahDetail
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SurahSearchResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

fun SuarahDataDto.toDomain() = Surah(
    number = nomor,
    namaLatin = namaLatin,
    nama = nama,
    arti = arti,
    jumlahAyat = jumlahAyat
)

fun AyatDto.toDomain() = Ayat(
    nomorAyat = nomorAyat,
    teksArab = teksArab,
    teksLatin = teksLatin,
    teksIndonesia = teksIndonesia,
    audioUrl = audio.x01 // using 01 as default audio (Abdullah Al-Juhany)
)

fun SurahDetailDto.toDomain() = SurahDetail(
    number = nomor,
    namaLatin = namaLatin,
    nama = nama,
    arti = arti,
    jumlahAyat = jumlahAyat,
    ayat = ayat?.map { it.toDomain() } ?: emptyList()
)

private val searchJson = Json { ignoreUnknownKeys = true }

fun AyatSearchDataDto.toDomainWithAudio(): Ayat = Ayat(
    nomorAyat = nomorAyat,
    teksArab = teksArab,
    teksLatin = teksLatin,
    teksIndonesia = terjemahanId,
    audioUrl = buildAudioUrl(idSurat, nomorAyat)
)

fun SearchResultDto.toSearchResult(): SearchResult = when (tipe) {
    "surat" -> {
        val surahData = searchJson.decodeFromJsonElement<SuarahDataDto>(data)
        SurahSearchResult(surah = surahData.toDomain(), score = skor)
    }
    "ayat" -> {
        val ayatData = searchJson.decodeFromJsonElement<AyatSearchDataDto>(data)
        AyatSearchResult(
            ayat = ayatData.toDomainWithAudio(),
            surahName = ayatData.namaSurat,
            surahNumber = ayatData.idSurat,
            score = skor
        )
    }
    else -> throw IllegalArgumentException("Unknown search result type: $tipe")
}

private fun buildAudioUrl(surahNumber: Int, ayatNumber: Int): String {
    val surah = surahNumber.toString().padStart(3, '0')
    val ayat = ayatNumber.toString().padStart(3, '0')
    return "https://equran.id/api/audio/$surah/$ayat.mp3"
}