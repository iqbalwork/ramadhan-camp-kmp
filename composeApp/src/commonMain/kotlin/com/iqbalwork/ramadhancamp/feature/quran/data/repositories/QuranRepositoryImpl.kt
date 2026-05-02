package com.iqbalwork.ramadhancamp.feature.quran.data.repositories

import com.iqbalwork.ramadhancamp.feature.quran.data.datasource.QuranRemoteDatasource
import com.iqbalwork.ramadhancamp.feature.quran.data.mapper.toDomain
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Surah
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SurahDetail
import com.iqbalwork.ramadhancamp.feature.quran.domain.repository.QuranRepository

class QuranRepositoryImpl(
    private val remoteDatasource: QuranRemoteDatasource
) : QuranRepository {
    override suspend fun getSurahs(): Result<List<Surah>> {
        return remoteDatasource.getSurahs().map { response ->
            response.data.map { it.toDomain() }
        }
    }

    override suspend fun getSurahDetail(nomor: Int): Result<SurahDetail> {
        return remoteDatasource.getSurahDetail(nomor).map { response ->
            response.data.toDomain()
        }
    }

    override suspend fun searchSurah(query: String): Result<List<Surah>> {
        return remoteDatasource.searchSurah(query).map { response ->
            response.hasil.filter { it.tipe == "surat" }.map { it.data.toDomain() }
        }
    }
}
