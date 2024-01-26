package com.ekaterinabaygin.movie.feature.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekaterinabaygin.domain.entity.MovieEntity
import com.ekaterinabaygin.movie.databinding.ItemMovieBinding
import com.ekaterinabaygin.movie.feature.main.MovieRecyclerAdapter.MovieViewHolder

private object MovieDiffCallback : DiffUtil.ItemCallback<MovieEntity>() {
    override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
        return oldItem == newItem
    }
}

class MovieRecyclerAdapter : ListAdapter<MovieEntity, MovieViewHolder>(MovieDiffCallback) {
    private var onMovieClickListener: ((MovieEntity) -> Unit)? = null

    fun setOnMovieClickListener(listener: (MovieEntity) -> Unit) {
        onMovieClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieEntity) {
            itemView.setOnClickListener { onMovieClickListener?.invoke(movie) }

            Glide.with(itemView.context)
                .load(movie.posterUrl)
                .into(binding.imageViewPoster)
        }
    }
}