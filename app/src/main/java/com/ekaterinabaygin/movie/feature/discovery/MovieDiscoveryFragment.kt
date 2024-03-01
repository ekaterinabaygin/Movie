package com.ekaterinabaygin.movie.feature.discovery

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
import com.ekaterinabaygin.movie.databinding.FragmentMoviesBinding
import kotlinx.coroutines.launch


class MovieDiscoveryFragment : Fragment() {

    companion object {
        fun newInstance() = MovieDiscoveryFragment()
    }

    interface DiscoveryActionCallback {
        fun showDetails(movie: MoviePreview)
    }

    private var actionCallback: DiscoveryActionCallback? = null
    private val viewModel by viewModels<MovieDiscoveryViewModel> {
        MovieDiscoveryViewModelFactory(requireContext().applicationContext)
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = requireNotNull(_binding)

    private val moviesAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MovieRecyclerAdapter(
            onMovieClickListener = { movie -> actionCallback?.showDetails(movie) },
            switchFavouriteStatus = { movie -> viewModel.switchFavouriteStatus(movie) },
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        actionCallback = context as? DiscoveryActionCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
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
        errorButton.setOnClickListener { viewModel.loadMovies() }
        parent.setOnRefreshListener { viewModel.loadMovies() }
    }

    private fun observeState() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { it.moviesState.handleMoviesState() }
            }
        }
    }

    private fun MovieDiscoveryUIState.MovieState.handleMoviesState() = when (this) {
        is MovieDiscoveryUIState.MovieState.Data -> {
            binding.moviesList.visibility = View.VISIBLE
            binding.parent.isRefreshing = false
            binding.errorText.visibility = View.GONE
            binding.errorButton.visibility = View.GONE
            moviesAdapter.submitList(movies)
        }

        MovieDiscoveryUIState.MovieState.Loading -> {
            binding.moviesList.visibility = View.GONE
            binding.parent.isRefreshing = true
            binding.errorText.visibility = View.GONE
            binding.errorButton.visibility = View.GONE
        }

        MovieDiscoveryUIState.MovieState.Error -> {
            binding.moviesList.visibility = View.GONE
            binding.parent.isRefreshing = false
            binding.errorText.visibility = View.VISIBLE
            binding.errorButton.visibility = View.VISIBLE
        }
    }
}
