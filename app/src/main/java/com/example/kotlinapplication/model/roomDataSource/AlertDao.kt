package com.example.kotlinapplication.model.roomDataSource

import androidx.room.*

@Dao
interface AlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alert: AlertEntity)

    @Query("SELECT * FROM Alert")
    fun getAll(): List<AlertEntity>

    @Query("SELECT * FROM Alert WHERE enabled=:enabled and alertTime=:alertTime")
    fun getSome(alertTime: String, enabled: Boolean): List<AlertEntity>

    @Delete
    suspend fun delete(alert: AlertEntity)
}