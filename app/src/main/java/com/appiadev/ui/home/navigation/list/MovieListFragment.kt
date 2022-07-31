package com.appiadev.ui.home.navigation.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appiadev.binding.submitMovieItems
import com.appiadev.databinding.FragmentMovieListBinding
import com.appiadev.ui.home.navigation.list.adapter.MovieListAdapter
import com.appiadev.utils.launchAndRepeatWithViewLifecycle
import com.appiadev.utils.showProgressBar
import com.appiadev.viewModel.UniversalViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding
    private val viewModel by viewModel<UniversalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieListBinding.inflate(inflater, container, false)
        binding.rvMovieList.adapter = MovieListAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllMovies()
        launchAndRepeatWithViewLifecycle {
            binding.progressBar.showProgressBar(false)
            viewModel.movieList.observe(viewLifecycleOwner) {
                binding.rvMovieList.submitMovieItems(it)
            }
        }
    }
}