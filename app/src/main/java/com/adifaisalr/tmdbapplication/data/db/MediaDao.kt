package com.adifaisalr.tmdbapplication.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.adifaisalr.tmdbapplication.libs.domain.model.Media

/**
 * Interface for database access.
 */
@Dao
abstract class MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMedia(media: Media): Long

    @Query("DELETE FROM `Media` WHERE id = :id")
    abstract suspend fun deleteMedia(id: Int): Int

    @Query("SELECT * FROM `Media`")
    abstract fun loadAllMedias(): List<Media>

    @Query("SELECT * FROM `Media` WHERE id = :id")
    abstract fun loadMediaById(id: Int): Media?

    @Query("SELECT * FROM `Media` WHERE isFavorite = 1")
    abstract fun loadAllFavoriteMedias(): List<Media>

    @Query("SELECT * FROM `Media` WHERE isFavorite = 1 AND id = :id")
    abstract fun loadFavoriteMediaById(id: Int): Media?

    @Update
    abstract suspend fun updateMedia(media: Media): Int
}
