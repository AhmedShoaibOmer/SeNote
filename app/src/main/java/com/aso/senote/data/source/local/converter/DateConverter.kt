/*
 * Copyright (c) ASO 2020.
 */
package com.aso.senote.data.source.local.converter

import androidx.room.TypeConverter
import java.util.*

object DateConverter {
    @JvmStatic
    @TypeConverter
    fun calenderToDateStamp(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @JvmStatic
    @TypeConverter
    fun dateStampToCalender(value: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = value
        return calendar
    }
}