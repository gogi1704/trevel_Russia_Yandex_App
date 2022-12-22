package com.example.trevel_russia_yandex_app.application

import android.app.Application
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.graphics.drawable.toBitmap
import androidx.room.Room
import com.example.trevel_russia_yandex_app.R
import com.example.trevel_russia_yandex_app.constans.MAP_API_KEY
import com.example.trevel_russia_yandex_app.db.AppDatabase
import com.yandex.mapkit.MapKitFactory


class YandexApplication() : Application() {


    init {

        MapKitFactory.setApiKey(MAP_API_KEY)

    }

}