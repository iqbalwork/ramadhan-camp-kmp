package com.iqbalwork.ramadhancamp.feature.quran.data.mapper

import com.iqbalwork.ramadhancamp.feature.home.data.model.surah.SuarahDataDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.AyatDto
import com.iqbalwork.ramadhancamp.feature.quran.data.model.SurahDetailDto
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Ayat
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Surah
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SurahDetail

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
