package com.example.smartmessenger.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartmessenger.*
import com.example.smartmessenger.LiveEvent
import com.example.smartmessenger.MutableLiveEvent
import com.hfad.fitness.async.ErrorResult
import com.hfad.fitness.async.Result
import com.hfad.fitness.async.SuccessResult
import kotlinx.coroutines.*

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

    fun <T> into(liveResult: MutableLiveData<Result<T>>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            } catch (e: Exception) {
                if (e !is CancellationException) liveResult.postValue(ErrorResult(e))
            }
        }
    }
}

fun <T> LiveData<T>.requireValue(): T {
    return this.value ?: throw IllegalStateException("Value is empty")
}