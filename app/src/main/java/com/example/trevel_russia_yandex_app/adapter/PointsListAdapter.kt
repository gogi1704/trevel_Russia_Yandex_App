package com.example.trevel_russia_yandex_app.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trevel_russia_yandex_app.databinding.ItemLayoutBinding
import com.example.trevel_russia_yandex_app.model.PointModel

interface OnItemClickListener {
    fun onClick(id: Int)
    fun delete(id:Int)
    fun edit(id:Int)
}

class PointsListAdapter(private val listener: OnItemClickListener) :
    ListAdapter<PointModel, PointsListAdapter.PointsViewHolder>(PostDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointsViewHolder {
        val binding =
            ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PointsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointsViewHolder, position: Int) {
        val point = getItem(position)
        holder.bind(point, listener)

    }

    class PointsViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pointModel: PointModel, listener: OnItemClickListener) {
            with(binding) {
                textTitle.text = pointModel.title
                textContent.text = pointModel.content
                itemView.setOnClickListener {
                    listener.onClick(pointModel.id)
                }
                imageDelete.setOnClickListener {
                    listener.delete(pointModel.id)
                }
                imageEdit.setOnClickListener {
                    listener.edit(pointModel.id)
                }
            }
        }
    }

}