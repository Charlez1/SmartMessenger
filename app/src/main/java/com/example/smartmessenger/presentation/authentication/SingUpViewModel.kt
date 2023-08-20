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
import com.example.smartmessenger.domain.entity.SignUpData
import com.example.smartmessenger.utils.AccountAlreadyExistsException
import com.example.smartmessenger.utils.AuthWeakPasswordException
import com.example.smartmessenger.utils.EmptyFieldException
import com.example.smartmessenger.utils.NonUniqueNameException
import com.example.smartmessenger.utils.PasswordMismatchException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingUpViewModel @Inject constructor(
    private val accountsRepository: AccountsRepository
) : BaseViewModel() {

    private val _state = MutableLiveData(State())
    val state: LiveData<State> = _state

    private val _showErrorToastEvent = MutableLiveEvent<Int>()
    val showErrorToastEvent: LiveEvent<Int> = _showErrorToastEvent

    private val _showSuccessToastEvent = MutableLiveEvent<Int>()
    val showSuccessToastEvent: LiveEvent<Int> = _showSuccessToastEvent

    private val _navigateToSingIn = MutableLiveEvent<Unit>()
    val navigateToSingIn: LiveEvent<Unit> = _navigateToSingIn

    fun singUp(singUpData: SignUpData) = safeLaunch {
        showProgress()
        try {
            accountsRepository.signUp(singUpData)
            launchSignInScreen()
            showSuccessToast(R.string.registration_is_success)
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: IllegalArgumentException) {
            showErrorToast(R.string.username_illegal_argument_error)
        } catch (e: PasswordMismatchException) {
            showErrorToast(R.string.password_mismatch_error)
        } catch (e: NonUniqueNameException) {
            showErrorToast(R.string.username_non_unique_error)
        } catch (e: AuthWeakPasswordException) {
            showErrorToast(R.string.auth_weak_password_error)
        } catch (e: AccountAlreadyExistsException) {
            showErrorToast(R.string.account_already_exists_error)
        } finally {
            hideProgress()
        }
    }

    private fun showProgress() {
        _state.value = State(signUpInProgress = true)
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

    private fun showErrorToast(errorMessageRes: Int) = _showErrorToastEvent.publishEvent(errorMessageRes)

    private fun showSuccessToast(messageRes: Int) = _showSuccessToastEvent.publishEvent(messageRes)

    private fun launchSignInScreen() = _navigateToSingIn.publishEvent(Unit)

    data class State(
        val emptyUsernameError: Boolean = false,
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val emptyPasswordRepeatError: Boolean = false,
        val signUpInProgress: Boolean = false
    )
}