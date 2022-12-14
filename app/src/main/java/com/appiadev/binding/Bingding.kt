package com.appiadev.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.appiadev.model.api.Movie
import com.appiadev.ui.home.navigation.list.adapter.MovieListAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["submitItems"])
fun RecyclerView.submitMovieItems(movieList: List<Movie>?) {
    if (movieList == null || movieList.isEmpty()) return
    if (this.adapter is MovieListAdapter) {
        (this.adapter as MovieListAdapter).submitList(movieList)
    }
}

@BindingAdapter(value = ["imageUrl"])
fun ImageView.setImageUrl(uri: String?) {
    Glide.with(this.context)
        .load(uri)
        //.skipMemoryCache(true)
        .onlyRetrieveFromCache(false)
        .into(this)
}