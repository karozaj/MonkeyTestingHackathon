package com.example.monkeytestinghackathon.utils

import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return format.format(date)
}

fun currentTimeToLong(): Long {
    return System.currentTimeMillis()
}

fun convertDateToLong(date: String): Long {
    val df = SimpleDateFormat("yyyy.MM.dd HH:mm")
    return df.parse(date).time
}

fun stringToDate(isoString: String): Date {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.parse(isoString)
    } catch (e: Exception) {
        return Date(0)
    }
}