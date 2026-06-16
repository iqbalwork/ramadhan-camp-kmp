package com.iqbalwork.ramadhancamp.shared.common.utils.math

import kotlin.math.PI
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val R = 6371.0

fun toRadians(deg: Double): Double = deg / 180.0 * PI

fun toDegrees(rad: Double): Double = rad * 180.0 / PI


fun haversineDistanceKm(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val dLat = toRadians(lat2 - lat1)
    val dLng = toRadians(lng2 - lng1)
    val a = sin(dLat / 2).pow(2) +
            cos(toRadians(lat1)) * cos(toRadians(lat2)) * sin(dLng / 2).pow(2)
    return R * 2 * asin(sqrt(a))
}