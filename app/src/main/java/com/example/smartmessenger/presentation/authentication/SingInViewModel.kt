package com.example.smartmessenger.presentation.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.smartmessenger.*
import com.example.smartmessenger.presentation.BaseViewModel
import com.example.smartmessenger.presentation.requireValue
import com.example.smartmessenger.utils.Field
import com.example.smartmessenger.utils.LiveEvent
import com.example.smartmessenger.utils.MutableLiveEvent
import com.example.smartmessenger.utils.publishEvent
import com.example.smartmessenger.domain.repository.account.AccountsRepository
import com.example.smartmessenger.utils.EmptyFieldException
import com.example.smartmessenger.utils.InvalidUserException
import com.example.smartmessenger.utils.TooManyRequestsException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingInViewModel @Inject constructor(
    private val accountsRepository: AccountsRepository
) : BaseViewModel() {

    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state

    private val _showErrorToastEvent = MutableLiveEvent<Int>()
    val showErrorToastEvent: LiveEvent<Int> = _showErrorToastEvent

    private val _clearPasswordEvent = MutableLiveEvent<Unit>()
    val clearPasswordEvent: LiveEvent<Unit> = _clearPasswordEvent

    private val _navigateToChatList = MutableLiveEvent<Unit>()
    val navigateToChatList: LiveEvent<Unit> = _navigateToChatList

    fun singIn(email: String, password: String, rememberUser: Boolean) = safeLaunch {
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

    private fun showProgress() {
        _state.postValue(State(signInInProgress = true))
    }

    private fun hideProgress() {
        _state.postValue(_state.requireValue().copy(
            signInInProgress = false
        ))
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.postValue(_state.requireValue().copy(
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password
        ))
    }

    private fun showErrorToast(errorMessageRes: Int) = _showErrorToastEvent.publishEvent(errorMessageRes)

    private fun clearPasswordField() = _clearPasswordEvent.publishEvent(Unit)

    private fun launchChatListScreen() = _navigateToChatList.publishEvent(Unit)

    data class State(
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val signInInProgress: Boolean = false
    )
}