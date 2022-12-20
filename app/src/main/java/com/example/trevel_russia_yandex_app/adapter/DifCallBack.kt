package com.example.trevel_russia_yandex_app.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.trevel_russia_yandex_app.model.PointModel

class PostDiffCallback : DiffUtil.ItemCallback<PointModel>() {
    override fun areItemsTheSame(oldItem: PointModel, newItem: PointModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PointModel, newItem: PointModel): Boolean {
        return oldItem == newItem
    }
}