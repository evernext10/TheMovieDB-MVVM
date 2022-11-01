package com.appiadev.model.core

sealed class MovieFilterType {
    object Language : MovieFilterType()
    object Year : MovieFilterType()
}
