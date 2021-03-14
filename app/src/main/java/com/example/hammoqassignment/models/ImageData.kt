package com.example.hammoqassignment.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageData(
    @SerialName("total_results") val totalResults: Long? = 0,
    @SerialName("page") val page: Int,
    @SerialName("per_page") val perPage: Int,
    @SerialName("photos") val photos: List<Photo>,
    @SerialName("next_page") val nextPage: String? = null,
    @SerialName("prev_page") val prevPage: String? = null
)

@Serializable
data class Photo(
    @SerialName("id") val id: Long,
    @SerialName("width") val width: Int,
    @SerialName("height") val height: Int,
    @SerialName("url") val url: String,
    @SerialName("photographer") val photographer: String,
    @SerialName("photographer_url") val photographerUrl: String,
    @SerialName("photographer_id") val photographerId: Long,
    @SerialName("avg_color") val avgColor: String,
    @SerialName("liked") val liked: Boolean,
    @SerialName("src") val source: Source
)

@Serializable
data class Source(
    @SerialName("original") val original: String,
    @SerialName("large2x") val largeImage: String,
    @SerialName("large") val large: String,
    @SerialName("medium") val medium: String,
    @SerialName("small") val small: String,
    @SerialName("portrait") val portrait: String,
    @SerialName("landscape") val landscape: String,
    @SerialName("tiny") val tiny: String
)