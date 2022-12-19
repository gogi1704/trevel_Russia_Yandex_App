package com.example.trevel_russia_yandex_app.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trevel_russia_yandex_app.db.PointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MapDao {


    @Query("SELECT * FROM PointEntity")
    suspend fun getAll(): List<PointEntity>

    @Insert
    suspend fun addPoint(pointModel: PointEntity)

    @Query("SELECT * FROM PointEntity WHERE id=(SELECT max(id) FROM PointEntity)")
    suspend fun getLast():PointEntity

    @Query("DELETE FROM PointEntity WHERE id = :id")
    suspend fun removePoint(id: Int)

    @Query("SELECT * FROM PointEntity WHERE id = :id")
    suspend fun getById(id: Int):PointEntity

}