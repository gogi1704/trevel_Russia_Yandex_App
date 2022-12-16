package com.example.trevel_russia_yandex_app.viewModels

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

class MapViewModel() : ViewModel() {


    fun createPlaceMark(
        context: Context,
        collection: MapObjectCollection,
        point: Point,
        placeMarkContent: String,
        iconBitmap: Bitmap
    ): PlacemarkMapObject =
        collection.addPlacemark(point).apply {
            setIcon(ImageProvider.fromBitmap(iconBitmap))
            opacity = 1F
            userData = placeMarkContent
            addTapListener { mapObject, point ->
                val text = mapObject.userData
                Toast.makeText(context, text.toString(), Toast.LENGTH_LONG).show()
                true
            }
        }
}