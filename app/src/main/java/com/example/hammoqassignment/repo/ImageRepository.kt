package com.example.hammoqassignment.repo

import com.example.hammoqassignment.models.ImageData
import com.example.hammoqassignment.remote.ApiService
import com.example.hammoqassignment.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ImageRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getImages(query: String): Flow<ImageData> {
        return flow {
            emit(apiService.getImages(query, Constants.ORIENTATION, Constants.PER_PAGE))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getNextImages(query: String): Flow<ImageData> {
        return flow {
            emit(
                apiService.getNextImages(
                    query,
                    Constants.ORIENTATION,
                    Constants.PER_PAGE,
                    Constants.NEXT_PAGE
                )
            )
        }.flowOn(Dispatchers.IO)
    }
}