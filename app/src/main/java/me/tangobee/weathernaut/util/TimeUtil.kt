package me.tangobee.weathernaut.util

import java.text.SimpleDateFormat
import java.util.Locale

class TimeUtil {

    companion object {
        fun extractTimeFromString(dateTimeString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
                val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                val date = inputFormat.parse(dateTimeString)
                outputFormat.format(date!!)
            } catch (e: Exception) {
                ""
            }
        }

        fun convertDateStringToDay(dateString: String): String? {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val date = inputFormat.parse(dateString)

                if (date != null) {
                    val outputFormat = SimpleDateFormat("EEEE", Locale.US)
                    return outputFormat.format(date)
                }
            } catch (_: Exception) {}

            return ""
        }

    }

}