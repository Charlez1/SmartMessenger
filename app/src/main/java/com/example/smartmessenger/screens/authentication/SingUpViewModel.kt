package com.example.smartmessenger.screens.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartmessenger.*
import com.example.smartmessenger.screens.BaseViewModel
import com.example.smartmessenger.screens.requireValue
import com.example.smartmessenger.model.Field
import com.example.smartmessenger.model.repositories.account.AccountsRepository

class SingUpViewModel(
    private val accountsRepository: AccountsRepository
) : BaseViewModel() {

    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state

    private val _showErrorToastEvent = MutableLiveEvent<Int>()
    val showErrorToastEvent: LiveEvent<Int> = _showErrorToastEvent

    private val _navigateToSingIn = MutableLiveEvent<Unit>()
    val navigateToSingIn: LiveEvent<Unit> = _navigateToSingIn

    fun singUp(singUpData: SignUpData) = viewModelScope.safeLaunch {
        setProgressVisible(true)
        try {
            accountsRepository.signUp(singUpData)
        } catch (e: PasswordMismatchException) {
             showPasswordMismatchToast()
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: AuthWeakPasswordException) {
            showAuthWeakPasswordToast()
        } catch (e: AccountAlreadyExistsException) {
            showAccountAlreadyExistsErrorToast()
        } finally {
            setProgressVisible(false)
        }
    }

    private fun setProgressVisible(show: Boolean) {
        _state.value = _state.requireValue().copy(
             signUpInProgress = show
        )
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = _state.requireValue().copy(
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password
        )
    }

    private fun showPasswordMismatchToast() = _showErrorToastEvent.publishEvent(R.string.test_error)

    private fun showAuthWeakPasswordToast() = _showErrorToastEvent.publishEvent(R.string.test_error)

    private fun showAccountAlreadyExistsErrorToast() = _showErrorToastEvent.publishEvent(R.string.test_error)



    data class State(
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val emptyPasswordRepeatError: Boolean = false,
        val signUpInProgress: Boolean = false
    )
}