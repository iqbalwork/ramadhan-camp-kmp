package com.iqbalwork.ramadhancamp.feature.quran.domain.model

sealed interface SearchResult {
    val score: Double
    val itemKey: String
}

data class SurahSearchResult(
    val surah: Surah,
    override val score: Double
) : SearchResult {
    override val itemKey: String get() = "surah-${surah.number}"
}

data class AyatSearchResult(
    val ayat: Ayat,
    val surahName: String,
    val surahNumber: Int,
    override val score: Double
) : SearchResult {
    override val itemKey: String get() = "ayat-$surahNumber-${ayat.nomorAyat}"
}
