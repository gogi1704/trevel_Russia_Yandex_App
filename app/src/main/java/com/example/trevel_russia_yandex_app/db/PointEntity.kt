package com.example.trevel_russia_yandex_app.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trevel_russia_yandex_app.model.PointModel
import com.yandex.mapkit.geometry.Point

@Entity
data class PointEntity(
   @PrimaryKey(autoGenerate = true) val id:Int ,
   @ColumnInfo(name = "title") val title:String,
   @ColumnInfo(name ="content") val content:String?,
   @ColumnInfo(name ="latitude") val latitude:Double,
   @ColumnInfo(name ="longitude") val longitude:Double,
){

   fun toPointModel() = PointModel(id, title, content, latitude, longitude)

   fun toPoint() = Point(latitude, longitude)

   companion object {
      fun fromPointModel(pointEntity: PointEntity) = PointEntity(
         pointEntity.id,
         pointEntity.title,
         pointEntity.content,
         pointEntity.latitude,
         pointEntity.longitude
      )
   }
}