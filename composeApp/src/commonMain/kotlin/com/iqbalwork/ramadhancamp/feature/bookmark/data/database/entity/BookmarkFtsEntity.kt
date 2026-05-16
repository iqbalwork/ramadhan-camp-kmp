package com.iqbalwork.ramadhancamp.feature.bookmark.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4(contentEntity = BookmarkEntity::class)
@Entity(tableName = "bookmark_fts")
data class BookmarkFtsEntity(
    @ColumnInfo(name = "ayah_details")
    val ayahDetails: String,
    @ColumnInfo(name = "surah_name")
    val surahName: String
)