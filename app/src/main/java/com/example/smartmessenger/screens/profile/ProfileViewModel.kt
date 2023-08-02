package com.example.smartmessenger.screens.profile

import androidx.lifecycle.ViewModel
import com.example.smartmessenger.model.repositories.currentchat.CurrentChatRepository
import com.example.smartmessenger.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: CurrentChatRepository
) : BaseViewModel() {
    // TODO: Implement the ViewModel
}