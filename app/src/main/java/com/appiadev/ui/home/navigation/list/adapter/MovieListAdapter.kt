package com.appiadev.ui.home.navigation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appiadev.binding.setImageUrl
import com.appiadev.databinding.LayoutMovieListItemBinding
import com.appiadev.model.core.Movie

class MovieListAdapter : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(MovieDiffUtil()) {
    companion object {
        private class MovieDiffUtil : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutMovieListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(private val viewItem: LayoutMovieListItemBinding) :
        RecyclerView.ViewHolder(viewItem.root) {

        fun bind(movie: Movie) {
            viewItem.movie = movie
            viewItem.ivMovie.setImageUrl(movie.posterPath)
        }
    }
}
