package com.adifaisalr.tmdbapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adifaisalr.tmdbapplication.domain.model.Media

/**
 * Main database description.
 */
@Database(
    entities = [
        Media::class
    ],
    version = 1,
    exportSchema = true
)
abstract class MediaDb : RoomDatabase() {
    abstract fun mediaDao(): MediaDao
}
