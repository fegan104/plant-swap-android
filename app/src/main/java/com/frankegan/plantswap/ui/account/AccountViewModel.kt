package com.frankegan.plantswap.ui.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.frankegan.plantswap.data.PlantRepository
import com.frankegan.plantswap.data.UserRepository
import com.frankegan.plantswap.data.model.UserId
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class AccountViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val plantRepo: PlantRepository
): ViewModel() {


    fun currentUser() = userRepository.currentUser().asLiveData()

    fun signOut() = userRepository.signOut()

    fun userSignedIn() = viewModelScope.launch {
        userRepository.userSignedIn()
    }

    fun postsByUser() = userRepository.currentUser()
        .mapNotNull { it.getOrNull() }
        .flatMapLatest { user -> plantRepo.getUserPlantPosts(UserId(user.uid)) }
        .cachedIn(viewModelScope)
}