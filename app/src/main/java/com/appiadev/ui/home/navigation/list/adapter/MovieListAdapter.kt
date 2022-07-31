package com.appiadev.ui.home.navigation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appiadev.binding.setImageUrl
import com.appiadev.databinding.LayoutMovieListItemBinding
import com.appiadev.model.api.MovieResult

class MovieListAdapter(
    private val onClick: (MovieResult) -> Unit
) : ListAdapter<MovieResult, MovieListAdapter.MovieViewHolder>(MovieDiffUtil()) {
    companion object {
        private class MovieDiffUtil : DiffUtil.ItemCallback<MovieResult>() {
            override fun areItemsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieResult, newItem: MovieResult): Boolean {
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
            ),
            onClick
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MovieViewHolder(private val viewItem: LayoutMovieListItemBinding, val onClick: (MovieResult) -> Unit) :
        RecyclerView.ViewHolder(viewItem.root) {

        init {
            itemView.setOnClickListener {
                viewItem.movie?.let {
                    onClick(it)
                }
            }
        }

        fun bind(movie: MovieResult) {
            viewItem.movie = movie
            viewItem.ivMovie.setImageUrl(movie.posterPath)
        }
    }
}
