package com.example.newsfetch.db

import androidx.room.TypeConverter
import com.example.newsfetch.models.Source

class Convters {

    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name,name)
    }
}