package com.ekaterinabaygin.movie.feature.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ekaterinabaygin.movie.databinding.FragmentMoviesBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MovieFragment : Fragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = requireNotNull(_binding)

    private val moviesAdapter by lazy(LazyThreadSafetyMode.NONE) { MovieRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root;
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

    private fun setupView() {
        binding.moviesList.adapter = moviesAdapter
    }

    private fun observeState() {
        viewModel.state
            .onEach { it.moviesState.handleMoviesState() }
            .launchIn(lifecycleScope)
    }

    private fun MovieUIState.MovieState.handleMoviesState() = when (this) {
        is MovieUIState.MovieState.Data -> {
            binding.moviesList.visibility = View.VISIBLE
            binding.progressIndicator.visibility = View.GONE
            binding.errorText.visibility = View.GONE
            moviesAdapter.submitList(movies)
        }

        MovieUIState.MovieState.Loading -> {
            binding.moviesList.visibility = View.GONE
            binding.progressIndicator.visibility = View.VISIBLE
            binding.errorText.visibility = View.GONE
        }

        MovieUIState.MovieState.Error -> {
            binding.moviesList.visibility = View.GONE
            binding.progressIndicator.visibility = View.GONE
            binding.errorText.visibility = View.VISIBLE
        }
    }
}
