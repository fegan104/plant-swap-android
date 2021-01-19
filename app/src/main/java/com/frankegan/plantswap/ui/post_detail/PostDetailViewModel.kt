package com.frankegan.plantswap.ui.post_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.frankegan.plantswap.data.PlantRepository
import com.frankegan.plantswap.data.model.PlantPostId


class PostDetailViewModel @ViewModelInject constructor(
    private val plantRepo: PlantRepository
): ViewModel() {

    fun plantPost(id: PlantPostId) = liveData {
        emit(plantRepo.getPlantPost(id))
    }
}