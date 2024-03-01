package com.ekaterinabaygin.movie.feature.details

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.ekaterinabaygin.domain.entity.MovieDetails
import com.ekaterinabaygin.movie.databinding.FragmentDetailsBinding
import kotlinx.coroutines.launch

class MovieDetailsFragment : Fragment() {

    companion object {
        private const val ARG_MOVIE_ID = "movie_id"

        fun newInstance(movieId: String) = MovieDetailsFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_MOVIE_ID, movieId)
            }
        }
    }

    private val viewModel by viewModels<MovieDetailsViewModel> {
        MovieDetailsViewModelFactory(
            requireNotNull(requireArguments().getString(ARG_MOVIE_ID)),
            requireContext().applicationContext
        )
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() = with(binding) {
        errorButton.setOnClickListener { viewModel.loadDetails() }
        addToFavouriteButton.setOnClickListener { viewModel.switchFavouriteStatus() }
        backButton.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { it.moviesState.handleMoviesState() }
            }
        }
    }

    private fun MovieDetailsUIState.MovieState.handleMoviesState() = when (this) {
        is MovieDetailsUIState.MovieState.Data -> {
            details.handleMovie()
            binding.movieDetailsParent.visibility = View.VISIBLE
            binding.progressIndicator.visibility = View.GONE
            binding.errorParent.visibility = View.GONE
        }

        MovieDetailsUIState.MovieState.Loading -> {
            binding.movieDetailsParent.visibility = View.GONE
            binding.progressIndicator.visibility = View.VISIBLE
            binding.errorParent.visibility = View.GONE
        }

        MovieDetailsUIState.MovieState.Error -> {
            binding.movieDetailsParent.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
            binding.errorParent.visibility = View.VISIBLE
        }
    }

    private fun MovieDetails.handleMovie() = with(binding) {
        titleText.text = preview.title
        overviewText.text = overview
        addToFavouriteButton.setColorFilter(if (preview.isFavourite) Color.RED else Color.WHITE)
        Glide.with(requireContext())
            .load(preview.posterUrl)
            .into(imageViewPoster)
    }
}
