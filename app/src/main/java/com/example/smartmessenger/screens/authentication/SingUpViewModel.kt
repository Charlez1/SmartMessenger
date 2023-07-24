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
        showProgress()
        try {
            accountsRepository.signUp(singUpData)
            launchSignInScreen()
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: PasswordMismatchException) {
             showPasswordMismatchToast()
        } catch (e: AuthWeakPasswordException) {
            showAuthWeakPasswordToast()
        } catch (e: AccountAlreadyExistsException) {
            showAccountAlreadyExistsErrorToast()
        } finally {
            hideProgress()
        }
    }

    private fun showProgress() {
        _state.value = SingUpViewModel.State(signUpInProgress = true)
    }
    private fun hideProgress() {
        _state.value = _state.requireValue().copy(
            signUpInProgress = false
        )
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = _state.requireValue().copy(
            emptyUsernameError =  e.field == Field.Username,
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password,
            emptyPasswordRepeatError = e.field == Field.Password
        )
    }

    private fun showPasswordMismatchToast() = _showErrorToastEvent.publishEvent(R.string.password_mismatch_error)

    private fun showAuthWeakPasswordToast() = _showErrorToastEvent.publishEvent(R.string.auth_weak_password)

    private fun showAccountAlreadyExistsErrorToast() = _showErrorToastEvent.publishEvent(R.string.account_already_exists_error)

    private fun launchSignInScreen() = _navigateToSingIn.publishEvent(Unit)

    data class State(
        val emptyUsernameError: Boolean = false,
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val emptyPasswordRepeatError: Boolean = false,
        val signUpInProgress: Boolean = false
    )
}