package com.example.smartmessenger.presentation.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.smartmessenger.*
import com.example.smartmessenger.presentation.BaseFragment
import com.example.smartmessenger.databinding.FragmentSingUpBinding
import com.example.smartmessenger.utils.observeEvent
import com.example.smartmessenger.domain.entity.SignUpData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingUpFragment : BaseFragment(R.layout.fragment_sing_up) {

    override val viewModel by viewModels<SingUpViewModel>()
    private lateinit var binding: FragmentSingUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSingUpBinding.inflate(inflater, container, false)

        binding.signUpButton.setOnClickListener { createAccountPressed() }
        binding.signInButton.setOnClickListener { onSignInPressed() }

        observeFieldChangeToClearErrors()

        observeState()
        observeShowErrorToastEvent()
        observeShowSuccessToastEvent()
        observeNavigateToSingIn()

        return binding.root
    }

    private fun observeFieldChangeToClearErrors() {
        binding.usernameEditText.addTextChangedListener { binding.usernameTextInput.error = null }
        binding.emailEditText.addTextChangedListener { binding.emailTextInput.error = null }
        binding.passwordEditText.addTextChangedListener { binding.passwordTextInput.error = null }
        binding.repeatPasswordEditText.addTextChangedListener { binding.repeatPasswordTextInput.error = null }

    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
            binding.usernameTextInput.error = if (it.emptyUsernameError) getString(R.string.field_is_empty) else null
            binding.emailTextInput.error = if (it.emptyEmailError) getString(R.string.field_is_empty) else null
            binding.passwordTextInput.error = if (it.emptyPasswordError) getString(R.string.field_is_empty) else null
            binding.repeatPasswordTextInput.error = if (it.emptyPasswordRepeatError) getString(R.string.field_is_empty) else null

            val isEnableViews = !it.signUpInProgress
            binding.usernameTextInput.isEnabled = isEnableViews
            binding.emailTextInput.isEnabled = isEnableViews
            binding.passwordTextInput.isEnabled = isEnableViews
            binding.repeatPasswordTextInput.isEnabled = isEnableViews
            binding.signUpButton.isEnabled = isEnableViews
            binding.signInButton.isEnabled = isEnableViews

            binding.progressBar.visibility = if (it.signUpInProgress) View.VISIBLE else View.INVISIBLE
        }

    private fun observeShowErrorToastEvent() = viewModel.showErrorToastEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
    }

    private fun observeShowSuccessToastEvent() = viewModel.showSuccessToastEvent.observeEvent(viewLifecycleOwner) {
        Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
    }

    private fun observeNavigateToSingIn() = viewModel.navigateToSingIn.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

    private fun createAccountPressed() {
        viewModel.singUp(
            SignUpData(
                uniqueName = binding.usernameEditText.text.toString(),
                email = binding.emailEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                repeatPassword = binding.repeatPasswordEditText.text.toString())
        )
    }

    private fun onSignInPressed() {
        findNavController().popBackStack()
    }
}