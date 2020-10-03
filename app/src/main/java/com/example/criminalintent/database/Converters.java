package com.example.criminalintent.database;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class Converters {
    @TypeConverter
    public static Date timestampToDate(long timeStamp) {
        return new Date(timeStamp);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static String uuidToString(UUID uuid){
        return uuid.toString();
    }

    @TypeConverter
    public static UUID stringToUUID (String uuid){
        return UUID.fromString(uuid);
    }
}
