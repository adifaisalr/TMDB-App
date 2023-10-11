package com.adifaisalr.tmdbapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adifaisalr.tmdbapplication.domain.model.Media

/**
 * Interface for database access.
 */
@Dao
abstract class MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMedia(media: Media): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllMedias(cities: List<Media>): List<Long>

    @Query("DELETE FROM `Media`")
    abstract suspend fun deleteAllMedias(): Int

    @Query("SELECT * FROM `Media`")
    abstract fun getAllMedias(): List<Media>

    @Query("SELECT * FROM `Media` WHERE isFavorite = 1")
    abstract fun getFavoriteMedias(): List<Media>

    @Update
    abstract suspend fun updateMedia(media: Media): Int
}
