package com.iqbalwork.ramadhancamp.feature.home.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShalatScheduleDataDto(
    @SerialName("bulan")
    val bulan: Int,
    @SerialName("bulan_nama")
    val bulanNama: String,
    @SerialName("jadwal")
    val jadwal: List<ShalatJadwalDto>,
    @SerialName("kabkota")
    val kabkota: String,
    @SerialName("provinsi")
    val provinsi: String,
    @SerialName("tahun")
    val tahun: Int
)