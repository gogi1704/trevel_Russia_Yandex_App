package com.example.trevel_russia_yandex_app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.trevel_russia_yandex_app.db.PointEntity
import com.example.trevel_russia_yandex_app.model.PointModel

interface MapRepository {

    suspend fun getAll(): List<PointEntity>
    suspend fun addPoint(pointModel: PointEntity)
    suspend fun deletePoint(id: Int)
    suspend fun getPointById(id: Int):PointEntity

    suspend fun getLast():PointEntity
}