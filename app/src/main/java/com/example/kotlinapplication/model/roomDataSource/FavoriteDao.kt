package com.example.kotlinapplication.model.roomDataSource

import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("SELECT * FROM Favorite")
    fun getAll(): List<FavoriteEntity>

    @Delete
    suspend fun delete(favorite: FavoriteEntity)
}