package com.frankegan.plantswap.ui.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.frankegan.plantswap.data.UserRepository
import kotlinx.coroutines.launch

class AccountViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository
): ViewModel() {


    fun currentUser() = userRepository.currentUser().asLiveData()

    fun signOut() = userRepository.signOut()

    fun userSignedIn() = viewModelScope.launch {
        userRepository.userSignedIn()
    }
}