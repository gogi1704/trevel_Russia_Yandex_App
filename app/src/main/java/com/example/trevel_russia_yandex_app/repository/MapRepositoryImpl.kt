package com.example.trevel_russia_yandex_app.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.trevel_russia_yandex_app.db.PointEntity
import com.example.trevel_russia_yandex_app.db.AppDatabase
import com.example.trevel_russia_yandex_app.model.PointModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map


class MapRepositoryImpl(context: Context) : MapRepository {
    private val dao = AppDatabase.getInstance(context).mapDao()

    override suspend fun getAll(): List<PointEntity> = dao.getAll()

    override suspend fun addPoint(pointModel: PointEntity) = dao.addPoint(pointModel)

    override suspend fun deletePoint(id: Int) = dao.removePoint(id)

    override suspend fun getPointById(id: Int) = dao.getById(id)

    override suspend fun editPoint(id: Int, title: String, content: String) =
        dao.editPoint(id, title, content)

    override suspend fun getLast(): PointEntity = dao.getLast()


}