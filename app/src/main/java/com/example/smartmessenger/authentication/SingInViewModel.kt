package com.example.smartmessenger.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartmessenger.*
import com.example.smartmessenger.base.BaseViewModel
import com.example.smartmessenger.base.requireValue
import com.example.smartmessenger.model.Field
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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

    fun singIn(email: String, password: String) = viewModelScope.safeLaunch {
        setProgressVisible(true)
        try {
            accountsRepository.signIn(email, password)
            launchChatListScreen()
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: InvalidUserException) {
            showInvalidUserErrorToast()
            clearPasswordField()
        } catch (e: TooManyRequestsException) {
            showTooManyRequestsErrorToast()
            clearPasswordField()
        } finally {
            setProgressVisible(false)
        }
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = _state.requireValue().copy(
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password
        )
    }

    private fun clearPasswordField() = _clearPasswordEvent.publishEvent(Unit)

    private fun showInvalidUserErrorToast() = _showErrorToastEvent.publishEvent(R.string.invalid_user_error)

    private fun showTooManyRequestsErrorToast() = _showErrorToastEvent.publishEvent(R.string.too_many_requests_error)

    private fun launchChatListScreen() = _navigateToChatList.publishEvent(Unit)

    private fun setProgressVisible(show: Boolean) {
        _state.value = _state.requireValue().copy(
            signInInProgress = show
        )
    }

    data class State(
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val signInInProgress: Boolean = false
    )
}