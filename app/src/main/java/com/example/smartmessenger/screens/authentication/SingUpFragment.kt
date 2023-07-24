package com.example.smartmessenger.screens.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.smartmessenger.*
import com.example.smartmessenger.screens.BaseFragment
import com.example.smartmessenger.databinding.FragmentSingUpBinding
import com.example.smartmessenger.model.observeEvent
import com.example.smartmessenger.screens.createViewModel

class SingUpFragment : BaseFragment(R.layout.fragment_sing_up) {

    override val viewModel by createViewModel { SingUpViewModel(Singletons.accountsRepository) }
    private lateinit var binding: FragmentSingUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSingUpBinding.inflate(inflater, container, false)

        binding.signUpButton.setOnClickListener { createAccountPressed() }
        binding.signInButton.setOnClickListener { onSignInPressed() }

        binding.usernameEditText.addTextChangedListener { binding.usernameTextInput.error = null }
        binding.emailEditText.addTextChangedListener { binding.emailTextInput.error = null }
        binding.passwordEditText.addTextChangedListener { binding.passwordTextInput.error = null }
        binding.repeatPasswordEditText.addTextChangedListener { binding.repeatPasswordTextInput.error = null }

        observeState()
        observeShowErrorToastEvent()
        observeNavigateToSingIn()

        return binding.root
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
            binding.usernameTextInput.error = if (it.emptyUsernameError) getString(R.string.field_is_empty) else null
            binding.emailTextInput.error = if (it.emptyEmailError) getString(R.string.field_is_empty) else null
            binding.passwordTextInput.error = if (it.emptyPasswordError) getString(R.string.field_is_empty) else null
            binding.repeatPasswordTextInput.error = if (it.emptyPasswordRepeatError) getString(R.string.field_is_empty) else null

            val isEnableViews = !it.signUpInProgress
            binding.emailTextInput.isEnabled = isEnableViews
            binding.passwordTextInput.isEnabled = isEnableViews
            binding.repeatPasswordTextInput.isEnabled = isEnableViews
            binding.progressBar.visibility = if (it.signUpInProgress) View.VISIBLE else View.INVISIBLE
        }

    private fun observeNavigateToSingIn() = viewModel.navigateToSingIn.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

    private fun observeShowErrorToastEvent() = viewModel.showErrorToastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

    private fun createAccountPressed() {
        viewModel.singUp(
            SignUpData(
                username = binding.usernameEditText.text.toString(),
                email = binding.emailEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                repeatPassword = binding.repeatPasswordEditText.text.toString())
        )
    }

    private fun onSignInPressed() {
        findNavController().popBackStack()
    }
}