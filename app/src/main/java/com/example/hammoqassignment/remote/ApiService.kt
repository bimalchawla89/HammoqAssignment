package com.example.hammoqassignment.remote

import com.example.hammoqassignment.models.ImageData
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    suspend fun getImages(
        @Query("query") query: String,
        @Query("orientation") orientation: String,
        @Query("per_page") page: Int
    ): ImageData

    @GET("search")
    suspend fun getNextImages(
        @Query("query") query: String,
        @Query("orientation") orientation: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): ImageData
}