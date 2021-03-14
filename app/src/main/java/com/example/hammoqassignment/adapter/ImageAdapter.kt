package com.example.hammoqassignment.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.hammoqassignment.databinding.ImageItemBinding
import com.example.hammoqassignment.listener.ImageClickListener
import com.example.hammoqassignment.models.Photo
import com.example.hammoqassignment.viewbinding.BindingViewHolder
import com.example.hammoqassignment.viewbinding.createBindingViewHolder

typealias ImageViewHolder = BindingViewHolder<ImageItemBinding>

class ImageAdapter(private val imageClickListener: ImageClickListener) :
    ListAdapter<Photo, ImageViewHolder>(PhotoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return createBindingViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.image = getItem(position)
        holder.binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            imageClickListener.imageClicked(position)
        }
    }
}

class PhotoDiffCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}