package com.example.spacenews.utils

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object DateUtils {
    fun formatPublishedDate(dateString: String?): String? {
        if (dateString == null) return null
        return try {
            val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
            val outputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
            val zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter)
            outputFormatter.format(zonedDateTime)
        } catch (e: Exception) {
            dateString // Fallback to original string if parsing fails
        }
    }
}