/*
 * Copyright (c) ASO 2020.
 */

package com.aso.senote.data.source.local.converter;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class DateConverter {
    @TypeConverter
    public static Long calenderToDateStamp(Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    @TypeConverter
    public static Calendar dateStampToCalender(long value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        return calendar;
    }
}
