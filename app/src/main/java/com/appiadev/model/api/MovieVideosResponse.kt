package com.appiadev.model.api

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class MovieVideosResponse(
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("results")
    var results: List<Video>? = emptyList()
)

@Parcelize
@Entity(primaryKeys = [("id")])
data class Video(
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("key")
    var key: String? = null,
    @SerializedName("site")
    var site: String? = null,
    @SerializedName("size")
    var size: Int? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("published_at")
    var publishedAt: String? = null
) : Parcelable
