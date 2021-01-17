package com.frankegan.plantswap.ui.messages

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.frankegan.plantswap.data.PlantRepository
import com.frankegan.plantswap.data.UserRepository
import com.frankegan.plantswap.data.model.UserId
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull

class MessagesViewModel @ViewModelInject constructor(
    private val plantRepository: PlantRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    fun conversations() = userRepository.currentUser()
        .mapNotNull { result -> result.getOrNull() }
        .flatMapLatest { user ->
            plantRepository.getConversations(userId = UserId(user.uid))
        }.cachedIn(viewModelScope)
}