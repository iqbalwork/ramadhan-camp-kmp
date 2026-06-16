package com.iqbalwork.ramadhancamp.feature.bookmark.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmark",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["category_id"]),
        Index(value = ["surah_id", "ayat_number", "category_id"], unique = true)
    ]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0,
    @ColumnInfo(name = "ayah_details")
    val ayahDetails: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Long,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "surah_id")
    val surahId: Int,
    @ColumnInfo(name = "ayat_number")
    val ayatNumber: Int,
    @ColumnInfo(name = "surah_name")
    val surahName: String,
    @ColumnInfo(name = "audio_url")
    val audioUrl: String,
    @ColumnInfo(name = "teks_arab")
    val teksArab: String = "",
    @ColumnInfo(name = "teks_indonesia")
    val teksIndonesia: String = ""
)