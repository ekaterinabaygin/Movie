package com.ekaterinabaygin.movie

import androidx.recyclerview.widget.DiffUtil
import com.ekaterinabaygin.domain.MovieEntity

class MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem == newItem
    }
}
