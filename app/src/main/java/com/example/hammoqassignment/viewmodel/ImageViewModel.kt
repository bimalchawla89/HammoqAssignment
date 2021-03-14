package com.example.hammoqassignment.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hammoqassignment.extensions.toLiveData
import com.example.hammoqassignment.models.ImageData
import com.example.hammoqassignment.repo.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    ViewModel() {

    private val _imageData = MutableLiveData<ImageData?>()
    private val _nextImageData = MutableLiveData<ImageData?>()
    val imageListData = _imageData.toLiveData()
    val nextImageListData = _nextImageData.toLiveData()

    fun fetchImages(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            imageRepository.getImages(query).catch { e ->
                println("error "+e.localizedMessage)
                _imageData.postValue(null)
            }.collect {
                _imageData.postValue(it)
            }
        }
    }

    fun fetchNextImages(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            imageRepository.getNextImages(query).catch { e ->
                println("error "+e.localizedMessage)
                _nextImageData.postValue(null)
            }.collect {
                _nextImageData.postValue(it)
            }
        }
    }

}