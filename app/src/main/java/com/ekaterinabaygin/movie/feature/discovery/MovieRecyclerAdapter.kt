package com.ekaterinabaygin.movie.feature.discovery

import android.graphics.Color
import android.graphics.Matrix
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ekaterinabaygin.domain.entity.MoviePreview
import com.ekaterinabaygin.movie.databinding.ItemMovieBinding

private object MovieDiffCallback : DiffUtil.ItemCallback<MoviePreview>() {
    override fun areItemsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MoviePreview, newItem: MoviePreview): Boolean {
        return oldItem == newItem
    }
}

class MovieRecyclerAdapter(
    private val onMovieClickListener: (MoviePreview) -> Unit,
    private val switchFavouriteStatus: (MoviePreview) -> Unit
) : ListAdapter<MoviePreview, MovieRecyclerAdapter.MovieViewHolder>(MovieDiffCallback) {

    private val itemClickListener = View.OnClickListener {
        val movie = it.tag as? MoviePreview ?: return@OnClickListener
        onMovieClickListener(movie)
    }
    private val favouriteButtonClickListener = View.OnClickListener {
        val movie = it.tag as? MoviePreview ?: return@OnClickListener
        switchFavouriteStatus(movie)
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

        init {
            itemView.setOnClickListener(itemClickListener)
            binding.addToFavouriteButton.setOnClickListener(favouriteButtonClickListener)
        }

        fun bind(movie: MoviePreview) {
            itemView.tag = movie
            binding.addToFavouriteButton.tag = movie
            binding.titleText.text = movie.title

            binding.addToFavouriteButton.setColorFilter(if (movie.isFavourite) Color.RED else Color.WHITE)
            Glide.with(itemView.context)
                .load(movie.posterUrl)
                .into(binding.imageViewPoster)
        }
    }
}