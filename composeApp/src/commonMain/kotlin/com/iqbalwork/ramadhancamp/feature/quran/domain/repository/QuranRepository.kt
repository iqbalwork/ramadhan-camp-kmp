package com.iqbalwork.ramadhancamp.feature.quran.domain.repository

import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SearchResult
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Surah
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SurahDetail

interface QuranRepository {
    suspend fun getSurahs(): Result<List<Surah>>
    suspend fun getSurahDetail(nomor: Int): Result<SurahDetail>
    suspend fun search(query: String): Result<List<SearchResult>>
}