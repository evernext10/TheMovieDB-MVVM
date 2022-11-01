package com.appiadev.ui.home.navigation.detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.appiadev.R
import com.appiadev.binding.setImageUrl
import com.appiadev.databinding.FragmentMovieDetailBinding
import com.appiadev.model.api.Movie
import com.appiadev.model.api.Video
import com.appiadev.model.core.states.StateMovieDetail
import com.appiadev.model.core.states.StateMovieVideos
import com.appiadev.ui.home.navigation.detail.viewModel.MovieDetailViewModel
import com.appiadev.utils.launchAndRepeatWithViewLifecycle
import com.appiadev.utils.openFirstTrailerOnYoutube
import com.appiadev.utils.showAlertDialogErrorApi
import com.appiadev.utils.visible
import org.koin.android.viewmodel.ext.android.viewModel

class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var model: Movie
    private val viewModel by viewModel<MovieDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        model = requireArguments()["model"] as Movie
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovieDetailById(model.id!!)
        viewModel.getMovieTrailerById(model.id!!)
        observerState()
    }

    private fun observerState() {
        launchAndRepeatWithViewLifecycle {
            viewModel.movieDetail.observe(viewLifecycleOwner) {
                when (it) {
                    is StateMovieDetail.Loading -> {}
                    is StateMovieDetail.Success -> {
                        setDetailInformationToViews(it.movie)
                    }
                    is StateMovieDetail.Unauthorized -> {}
                    is StateMovieDetail.Error -> {
                        showAlertDialogErrorApi(findNavController())
                    }
                }
            }
            viewModel.movieVideos.observe(viewLifecycleOwner) {
                when (it) {
                    is StateMovieVideos.Loading -> {}
                    is StateMovieVideos.Success -> {
                        setTrailerButtonData(it.response.results)
                    }
                    is StateMovieVideos.Unauthorized -> {}
                    is StateMovieVideos.Error -> {
                        binding.labelSeeTrailer.visible(false)
                        binding.seeTrailerButton.visible(false)
                        showAlertDialogErrorApi(findNavController())
                    }
                }
            }
        }
    }

    private fun setTrailerButtonData(results: List<Video>?) = with(binding) {
        seeTrailerButton.setOnClickListener {
            if (results?.isNotEmpty() == true) {
                results[0].key?.let { key -> openFirstTrailerOnYoutube(key) }
            }
        }
    }

    private fun setDetailInformationToViews(movie: Movie) = with(binding) {
        titleMovie.text = movie.title
        plotOverview.text = movie.overview
        languageText.text = movie.originalLanguage
        averageText.text = movie.voteAverage.toString()
        yearText.text = movie.releaseDate?.split("-")?.get(0) ?: ""
        heroImage.setImageUrl(model.posterPath)
        toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }
}
