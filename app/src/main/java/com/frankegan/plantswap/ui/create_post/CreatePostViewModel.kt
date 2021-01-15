package com.frankegan.plantswap.ui.create_post

import android.net.Uri
import androidx.core.net.toUri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.frankegan.plantswap.data.PlantRepository
import com.frankegan.plantswap.data.UserRepository
import com.frankegan.plantswap.data.remote.model.CreatePlantPostResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

private const val PHOTOS_KEY = "photos"

class CreatePostViewModel @ViewModelInject constructor(
    private val plantRepo: PlantRepository,
    private val userRepo: UserRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val createPostStatus = MutableStateFlow<Result<CreatePlantPostResponse>?>(null)

    fun createPostStatus() = createPostStatus.filterNotNull().asLiveData()

    fun attachedPhotos() = savedStateHandle.getLiveData<List<Uri>>(PHOTOS_KEY)

    fun createPosts(postTitle: String, postDescription: String) = viewModelScope.launch {
        createPostStatus.value = plantRepo.createPosts(
            postTitle = postTitle,
            postDescription = postDescription,
            photoUris = savedStateHandle[PHOTOS_KEY],
            currentLocation = userRepo.getCurrentLocation().getOrNull()
        )
    }

    fun attachPhotos(newPhotos: List<Uri>) {
        val currentPhotos = savedStateHandle[PHOTOS_KEY] ?: emptyList<String>()
        savedStateHandle[PHOTOS_KEY] = currentPhotos + newPhotos
    }
}