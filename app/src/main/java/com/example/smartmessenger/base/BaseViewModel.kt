package com.example.smartmessenger.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmessenger.*
import com.example.smartmessenger.LiveEvent
import com.example.smartmessenger.MutableLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {
    private val _showErrorMessageResEvent = MutableLiveEvent<Int>()
    val showErrorMessageResEvent: LiveEvent<Int> = _showErrorMessageResEvent

    fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block.invoke(this)
            } catch (e: ConnectionException) {
                _showErrorMessageResEvent.publishEvent(R.string.connection_error)
            } catch (e: BackendException) {
                _showErrorMessageResEvent.publishEvent(R.string.backend_error)
            } catch (e: AuthException) {
                _showErrorMessageResEvent.publishEvent(R.string.auth_error)
            } catch (e: Exception) {
                _showErrorMessageResEvent.publishEvent(R.string.internal_error)
            }
        }
    }
}

fun <T> LiveData<T>.requireValue(): T {
    return this.value ?: throw IllegalStateException("Value is empty")
}