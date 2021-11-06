@file:JvmName("DateUtilExtensions")

package com.android.graphqlcmbnesample.util

import android.text.TextUtils
import android.text.format.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


const val PATTERN_SERVER_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val PATTERN_START_WITH_MONTH = "dd MMMM yyyy"



fun convertDateString(inputPattern: String, outputPattern: String, stringDate: String): String? {
    val originalFormat = SimpleDateFormat(inputPattern, Locale.getDefault())
    val targetFormat = SimpleDateFormat(outputPattern, Locale.getDefault())
    val requiredFormat = originalFormat.parse(stringDate)
    return requiredFormat?.let { targetFormat.format(requiredFormat) }
}
