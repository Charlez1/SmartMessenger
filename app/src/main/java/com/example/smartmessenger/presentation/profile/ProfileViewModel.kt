package com.example.smartmessenger.presentation.profile

import com.example.smartmessenger.domain.repository.currentchat.CurrentChatRepository
import com.example.smartmessenger.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: CurrentChatRepository
) : BaseViewModel() {
    // TODO: Implement the ViewModel
}