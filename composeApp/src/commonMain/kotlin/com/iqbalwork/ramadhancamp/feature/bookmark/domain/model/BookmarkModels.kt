package com.iqbalwork.ramadhancamp.feature.bookmark.domain.model

data class Category(
    val id: Long,
    val name: String
)

data class Bookmark(
    val id: Long,
    val ayahDetails: String,
    val categoryId: Long,
    val timestamp: Long,
    val surahId: Int,
    val ayatNumber: Int,
    val surahName: String,
    val audioUrl: String
)