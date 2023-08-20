package com.example.smartmessenger.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.smartmessenger.R
import com.example.smartmessenger.databinding.PartResultBinding
import com.example.smartmessenger.utils.observeEvent
import com.example.smartmessenger.utils.ErrorResult
import com.example.smartmessenger.utils.PendingResult
import com.example.smartmessenger.utils.Result
import com.example.smartmessenger.utils.SuccessResult

abstract class BaseFragment(@LayoutRes layoutId: Int): Fragment(layoutId) {

    abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showErrorMessageResEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    fun <T> renderResult(root: ViewGroup,
                         result: Result<T>,
                         onPending: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {

        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }

    }

    fun <T> renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
        val binding = PartResultBinding.bind(root)

        renderResult(
            root = root,
            result = result,
            onPending = {
                binding.progressBar.visibility = View.VISIBLE
            },
            onError = {
                binding.errorContainer.visibility = View.VISIBLE
            },
            onSuccess = { successData ->
                root.children
                    .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer }
                    .forEach { it.visibility = View.VISIBLE }
                onSuccess(successData)
            }
        )
    }
}