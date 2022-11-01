package com.appiadev.ui.home.navigation.list.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appiadev.R
import com.appiadev.binding.submitMovieItems
import com.appiadev.databinding.FragmentMovieListBinding
import com.appiadev.model.api.Movie
import com.appiadev.model.core.MovieFilterType
import com.appiadev.model.core.states.StateMovieList
import com.appiadev.ui.home.navigation.list.adapter.MovieListAdapter
import com.appiadev.ui.home.navigation.list.viewModel.MovieListViewModel
import com.appiadev.utils.launchAndRepeatWithViewLifecycle
import com.appiadev.utils.showAlertDialogErrorApi
import com.appiadev.utils.showToastMessage
import com.google.android.material.chip.Chip
import org.koin.android.viewmodel.ext.android.viewModel

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel by viewModel<MovieListViewModel>()
    private var chipChecked: Chip? = null

    private val adapterUpcoming: MovieListAdapter by lazy {
        MovieListAdapter(onClick = {
            navigateToMovieDetail(it)
        })
    }
    private val adapterTrends: MovieListAdapter by lazy {
        MovieListAdapter(onClick = {
            navigateToMovieDetail(it)
        })
    }
    private val adapterRecommended: MovieListAdapter by lazy {
        MovieListAdapter(onClick = {
            navigateToMovieDetail(it)
        })
    }

    private fun navigateToMovieDetail(movie: Movie) {
        findNavController().navigate(
            R.id.action_go_to_two,
            Bundle().apply {
                putParcelable(
                    "model",
                    movie
                )
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun loadMoreUpcoming(page: Int) = this.viewModel.postUpcomingMoviePage(page)
    private fun loadMoreTrends(page: Int) = this.viewModel.postTrendsMoviePage(page)
    private fun loadMoreRecommended(page: Int) = this.viewModel.postRecommendedMoviePage(page)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initViews()
        loadMoreUpcoming(page = 1)
        loadMoreTrends(page = 1)
        loadMoreRecommended(page = 1)
        observerState()
    }

    private fun observerState() {
        launchAndRepeatWithViewLifecycle {
            viewModel.upcomingMovieList.observe(viewLifecycleOwner) {
                when (it) {
                    is StateMovieList.Loading -> {
                        Log.i("upcomingMovieList", "Loading")
                    }
                    is StateMovieList.Success -> {
                        Log.i("upcomingMovieList", "Success")
                        binding.recyclerUpcoming.submitMovieItems(it.movies)
                    }
                    is StateMovieList.Error -> {
                        Log.e("upcomingMovieList", "Error")
                        showAlertDialogErrorApi(findNavController())
                    }
                    else -> {}
                }
            }
            viewModel.trendsMovieList.observe(viewLifecycleOwner) {
                when (it) {
                    is StateMovieList.Loading -> {
                        Log.i("trendsMovieList", "Loading")
                    }
                    is StateMovieList.Success -> {
                        Log.i("trendsMovieList", "Success")
                        binding.recyclerTrends.submitMovieItems(it.movies)
                    }
                    is StateMovieList.Error -> {
                        Log.e("trendsMovieList", "Error")
                        showAlertDialogErrorApi(findNavController())
                    }
                    else -> {}
                }
            }
            viewModel.recommendedMovieList.observe(viewLifecycleOwner) {
                when (it) {
                    is StateMovieList.Loading -> {
                        Log.i("recommendedMovieList", "Loading")
                    }
                    is StateMovieList.Success -> {
                        Log.i("recommendedMovieList", "Success")
                        binding.recyclerRecommended.submitMovieItems(it.movies.subList(0, 6))
                    }
                    is StateMovieList.Error -> {
                        Log.e("recommendedMovieList", "Error")
                        showAlertDialogErrorApi(findNavController())
                    }
                    else -> {}
                }
            }
            viewModel.showError.observe(viewLifecycleOwner) {
                if (it != null) {
                    showToastMessage(it)
                }
            }
        }
    }

    private fun FragmentMovieListBinding.initViews() {
        recyclerUpcoming.apply {
            adapter = adapterUpcoming
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        }
        recyclerTrends.apply {
            adapter = adapterTrends
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        }
        recyclerRecommended.adapter = adapterRecommended

        filterRecommendedMoviesChipsGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            checkedIds.map { chipId ->
                chipChecked = requireView().findViewById(chipId)
                when (chipId) {
                    R.id.filter_language -> {
                        if (chipChecked?.isChecked == true) {
                            viewModel.getRecommendedMoviesByFilter(MovieFilterType.Language)
                        }
                    }
                    R.id.filter_year -> {
                        if (chipChecked?.isChecked == true) {
                            viewModel.getRecommendedMoviesByFilter(MovieFilterType.Year)
                        }
                    }
                }
            }
        }
    }
}
