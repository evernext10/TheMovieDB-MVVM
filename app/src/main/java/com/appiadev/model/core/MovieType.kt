package com.appiadev.model.core

sealed class MovieType {
    object Upcoming : MovieType()
    object Trends : MovieType()
    object Recommended : MovieType()
}
