/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.database.offline;

import androidx.room.TypeConverter;

import java.util.Calendar;

class Converters {
    @TypeConverter
    static Long calenderToDateStamp(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    @TypeConverter
    static Calendar dateStampToCalender(long value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        return calendar;
    }
}
