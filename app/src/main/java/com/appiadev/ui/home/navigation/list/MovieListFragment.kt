package com.appiadev.ui.home.navigation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appiadev.R
import com.appiadev.binding.submitMovieItems
import com.appiadev.databinding.FragmentMovieListBinding
import com.appiadev.ui.home.navigation.list.adapter.MovieListAdapter
import com.appiadev.utils.NetworkManager
import com.appiadev.utils.launchAndRepeatWithViewLifecycle
import com.appiadev.utils.showProgressBar
import com.appiadev.utils.showToastMessage
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
        binding.rvMovieList.adapter = MovieListAdapter(onClick = {
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllMovies()
        binding.progressBar.showProgressBar(true)
        launchAndRepeatWithViewLifecycle {
            viewModel.movieList.observe(viewLifecycleOwner) {
                binding.progressBar.showProgressBar(false)
                binding.rvMovieList.submitMovieItems(it)
            }
            viewModel.showError.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.progressBar.showProgressBar(false)
                    showToastMessage(it)
                }
            }
        }
    }
}
