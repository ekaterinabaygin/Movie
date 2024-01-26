package com.ekaterinabaygin.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.ekaterinabaygin.domain.MovieEntity
import com.ekaterinabaygin.movie.databinding.ItemViewBinding

class MovieAdapter : ListAdapter<MovieEntity, MovieViewHolder>(MovieDiffCallback()) {
    private var onMovieClickListener: ((MovieEntity) -> Unit)? = null

    fun setOnMovieClickListener(listener: (MovieEntity) -> Unit) {
        onMovieClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        with(holder.binding) {
            Glide.with(holder.itemView.context).load(movie.posterPath).into(imageViewPoster)
        }

        holder.itemView.setOnClickListener { onMovieClickListener?.invoke(movie) }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }
}