package com.ekaterinabaygin.movie.feature.favourite

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ekaterinabaygin.domain.entity.MoviePreview
import com.ekaterinabaygin.movie.databinding.FragmentFavoritesBinding
import com.ekaterinabaygin.movie.feature.discovery.MovieRecyclerAdapter
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    interface FavoritesActionCallback {
        fun showDetails(movie: MoviePreview)
    }

    private var actionCallback: FavoritesActionCallback? = null
    private val viewModel by viewModels<FavoriteMoviesViewModel> {
        FavoriteMoviesViewModelFactory(requireContext().applicationContext)
    }

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = requireNotNull(_binding)

    private val moviesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MovieRecyclerAdapter(
            onMovieClickListener = { movie -> actionCallback?.showDetails(movie) },
            switchFavouriteStatus = { movie -> viewModel.switchFavouriteStatus(movie) },
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = context as? FavoritesActionCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
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

    override fun onDetach() {
        super.onDetach()
        actionCallback = null
    }

    private fun setupView() = with(binding) {
        moviesList.adapter = moviesAdapter
        moviesList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { it.moviesState.handleMoviesState() }
            }
        }
    }

    private fun FavoriteMoviesUIState.MovieState.handleMoviesState() = when (this) {
        is FavoriteMoviesUIState.MovieState.Data -> {
            binding.moviesList.visibility = View.VISIBLE
            binding.progressIndicator.visibility = View.GONE
            moviesAdapter.submitList(movies)
        }

        FavoriteMoviesUIState.MovieState.Loading -> {
            binding.moviesList.visibility = View.GONE
            binding.progressIndicator.visibility = View.VISIBLE
        }
    }
}
