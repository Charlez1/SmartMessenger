package com.example.smartmessenger.screens.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartmessenger.*
import com.example.smartmessenger.screens.BaseViewModel
import com.example.smartmessenger.screens.requireValue
import com.example.smartmessenger.model.Field
import com.example.smartmessenger.model.LiveEvent
import com.example.smartmessenger.model.MutableLiveEvent
import com.example.smartmessenger.model.publishEvent
import com.example.smartmessenger.model.repositories.account.AccountsRepository

class SingInViewModel(
    private val accountsRepository: AccountsRepository
) : BaseViewModel() {

    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state

    private val _clearPasswordEvent = MutableLiveEvent<Unit>()
    val clearPasswordEvent: LiveEvent<Unit> = _clearPasswordEvent

    private val _showErrorToastEvent = MutableLiveEvent<Int>()
    val showErrorToastEvent: LiveEvent<Int> = _showErrorToastEvent

    private val _navigateToChatList = MutableLiveEvent<Unit>()
    val navigateToChatList: LiveEvent<Unit> = _navigateToChatList

    fun singIn(email: String, password: String, rememberUser: Boolean) = viewModelScope.safeLaunch {
        showProgress()
        try {
            accountsRepository.signIn(email, password, rememberUser)
            launchChatListScreen()
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: InvalidUserException) {
            showErrorToast(R.string.invalid_user_error)
            clearPasswordField()
        } catch (e: TooManyRequestsException) {
            showErrorToast(R.string.too_many_requests_error)
            clearPasswordField()
        } finally {
            hideProgress()
        }
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = _state.requireValue().copy(
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password
        )
    }
    private fun clearPasswordField() = _clearPasswordEvent.publishEvent(Unit)

    private fun showErrorToast(errorMessageRes: Int) = _showErrorToastEvent.publishEvent(errorMessageRes)

    private fun launchChatListScreen() = _navigateToChatList.publishEvent(Unit)

    private fun showProgress() {
        _state.value = State(signInInProgress = true)
    }

    private fun hideProgress() {
        _state.value = _state.requireValue().copy(
            signInInProgress = false
        )
    }

    data class State(
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val signInInProgress: Boolean = false
    )
}