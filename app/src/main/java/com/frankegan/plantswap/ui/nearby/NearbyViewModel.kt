package com.frankegan.plantswap.ui.nearby

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.frankegan.plantswap.data.PlantRepository
import com.frankegan.plantswap.data.UserRepository
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.extensions.flatMap
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.launch

class NearbyViewModel @ViewModelInject constructor(
    private val plantRepo: PlantRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    fun nearbyPlantPosts(): LiveData<Result<List<PlantPost>>> = liveData {
        val result = userRepo.getCurrentLocation().flatMap { location ->
            plantRepo.getNearbyPlants(location)
        }

        emit(result)
    }
}