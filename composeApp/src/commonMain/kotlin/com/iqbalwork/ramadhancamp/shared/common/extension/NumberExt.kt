package com.iqbalwork.ramadhancamp.shared.common.extension

/**
 * Formats a number with leading zeros up to the specified length.
 * Defaults to a 2-digit format (e.g., 5 -> "05").
 */
fun Number.padZero(length: Int = 2): String = this.toString().padStart(length, '0')

