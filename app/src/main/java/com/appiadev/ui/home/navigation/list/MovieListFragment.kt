package com.appiadev.ui.home.navigation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appiadev.R
import com.appiadev.binding.submitMovieItems
import com.appiadev.databinding.FragmentMovieListBinding
import com.appiadev.model.core.State
import com.appiadev.pagging.RecyclerViewPaginator
import com.appiadev.ui.home.navigation.list.adapter.MovieListAdapter
import com.appiadev.utils.launchAndRepeatWithViewLifecycle
import com.appiadev.utils.showProgressBar
import com.appiadev.utils.showToastMessage
import com.appiadev.viewModel.UniversalViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel by viewModel<UniversalViewModel>()

    private val adapterUpcoming : MovieListAdapter by lazy {
        MovieListAdapter(onClick = {
            findNavController().navigate(
                R.id.action_go_to_two,
                Bundle().apply {
                    putParcelable(
                        "model",
                        it
                    )
                }
            )
        })
    }
    private val adapterTrends : MovieListAdapter by lazy {
        MovieListAdapter(onClick = {
            findNavController().navigate(
                R.id.action_go_to_two,
                Bundle().apply {
                    putParcelable(
                        "model",
                        it
                    )
                }
            )
        })
    }
    private val adapterRecommended : MovieListAdapter by lazy {
        MovieListAdapter(onClick = {
            findNavController().navigate(
                R.id.action_go_to_two,
                Bundle().apply {
                    putParcelable(
                        "model",
                        it
                    )
                }
            )
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding.recyclerUpcoming.apply {
            adapter = adapterUpcoming
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        }
        binding.recyclerTrends.apply {
            adapter = adapterTrends
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        }
        binding.recyclerRecommended.adapter = adapterRecommended

        RecyclerViewPaginator(
            recyclerView = binding.recyclerUpcoming,
            isLoading = { viewModel.showLoading.get() },
            loadMore = { loadMoreUpcoming(it) },
            onLast = { false }
        ).apply {
            threshold = 4
            currentPage = 1
        }

        RecyclerViewPaginator(
            recyclerView = binding.recyclerRecommended,
            isLoading = { viewModel.showLoading.get() },
            loadMore = { loadMoreRecommended(it) },
            onLast = { false }
        ).apply {
            threshold = 4
            currentPage = 1
        }

        RecyclerViewPaginator(
            recyclerView = binding.recyclerTrends,
            isLoading = { viewModel.showLoading.get() },
            loadMore = { loadMoreTrends(it) },
            onLast = { false }
        ).apply {
            threshold = 4
            currentPage = 1
        }

        return binding.root
    }

    private fun loadMoreUpcoming(page: Int) = this.viewModel.postUpcomingMoviePage(page)
    private fun loadMoreTrends(page: Int) = this.viewModel.postTrendsMoviePage(page)
    private fun loadMoreRecommended(page: Int) = this.viewModel.postRecommendedMoviePage(page)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMoreUpcoming(page = 1)
        loadMoreTrends(page = 1)
        loadMoreRecommended(page = 1)

        // binding.progressBar.showProgressBar(true)
        launchAndRepeatWithViewLifecycle {
            viewModel.upcomingMovieList.observe(viewLifecycleOwner) {
                when(it){
                    is State.Loading -> {
                        // binding.progressBar.showProgressBar(true)
                    }
                    is State.Success -> {
                        // binding.progressBar.showProgressBar(false)
                        binding.recyclerUpcoming.submitMovieItems(it.movies)
                    }
                    else -> {}
                }
            }
            viewModel.showError.observe(viewLifecycleOwner) {
                if (it != null) {
                    // binding.progressBar.showProgressBar(false)
                    showToastMessage(it)
                }
            }
        }
    }
}
