package com.frankegan.plantswap.ui.messages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.frankegan.plantswap.data.PlantRepository

class MessagesViewModel @ViewModelInject constructor(
    private val plantRepository: PlantRepository
) : ViewModel() {


}