package com.example.trevel_russia_yandex_app.viewModels

import android.app.Application
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import com.example.trevel_russia_yandex_app.R
import com.example.trevel_russia_yandex_app.db.PointEntity
import com.example.trevel_russia_yandex_app.model.PointModel
import com.example.trevel_russia_yandex_app.repository.MapRepositoryImpl
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.*

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val icon =
        AppCompatResources.getDrawable(
            application.applicationContext,
            R.drawable.ic_baseline_location_on_24
        )
            ?.toBitmap()!!
    private val repository = MapRepositoryImpl(application.applicationContext)
    var emptyPoint = Point()
    var emptyPointModel = PointModel(
        -1, "", "", 0.0, 0.0
    )
        set(value) {
            field = value
            pointLiveData.value = field
        }
    val pointLiveData = MutableLiveData(emptyPointModel)

    private var data = listOf<PointEntity>()
        set(value) {
            field = value
            pointListLiveData.value = value
        }
    val pointListLiveData = MutableLiveData(data)


    private val tap = MapObjectTapListener { mapObject, point ->
        getPointModelById(mapObject.userData.toString().toInt())

        true
    }

    init {
        viewModelScope.launch {
            data = repository.getAll()
        }

    }

    fun createPlaceMark(
        collection: MapObjectCollection,
        point: Point,
        placeMarkTitle: String,
        placeMarkContent: String,
        iconBitmap: Bitmap
    ) {
        viewModelScope.launch {

            repository.addPoint(
                PointEntity(
                    id = 0,
                    title = placeMarkTitle,
                    content = placeMarkContent,
                    latitude = point.latitude,
                    longitude = point.longitude
                )
            )

        }

        collection.addPlacemark(point).apply {
            viewModelScope.launch {
                setIcon(ImageProvider.fromBitmap(iconBitmap))
                opacity = 1F
                userData = if (data.isEmpty()) {
                    1
                } else data.last().id + 1
                addTapListener(tap)
            }

        }

        updateData()
        emptyPointModel = PointModel(
            -1, "", "", 0.0, 0.0
        )
    }

    fun addAllPlaceMarks(collection: MapObjectCollection) {

        viewModelScope.launch {

            for (point in data) {

                collection.addPlacemark(point.toPoint()).apply {
                    setIcon(ImageProvider.fromBitmap(icon))
                    opacity = 1F
                    userData = point.id
                    addTapListener(tap)
                }
            }
        }

    }

    fun editPoint(id: Int, title: String, content: String) {
        viewModelScope.launch {
            repository.editPoint(id, title, content)
        }
        updateData()
    }

    fun getPointById(id: Int): Point {
        val point = data.last { it.id == id }.toPointModel()
        return Point(point.latitude, point.longitude)
    }

    private fun getPointModelById(id: Int) {
        val pointModel = data.last { it.id == id }.toPointModel()
        emptyPointModel = pointModel

    }

    fun deletePoint(id: Int) {
        viewModelScope.launch {
            repository.deletePoint(id)
            updateData()
        }
    }

    fun updateData() {
        viewModelScope.launch {
            data = repository.getAll()
        }
    }

}