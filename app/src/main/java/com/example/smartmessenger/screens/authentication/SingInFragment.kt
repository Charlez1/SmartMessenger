package com.example.smartmessenger.screens.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.smartmessenger.*
import com.example.smartmessenger.screens.BaseFragment
import com.example.smartmessenger.databinding.FragmentSingInBinding

class SingInFragment : BaseFragment(R.layout.fragment_sing_in) {

    override val viewModel by createViewModel { SingInViewModel(Singletons.accountsRepository) }

    private lateinit var binding: FragmentSingInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSingInBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener { onSignInPressed() }
        binding.signUpButton.setOnClickListener { onSignUpPressed() }

        observeState()
        observeClearPasswordEvent()
        observeShowErrorToastEvent()
        observeNavigateToChatList()

        return binding.root
    }

    private fun observeState() = viewModel.state.observe(viewLifecycleOwner) {
            binding.emailTextInput.error = if (it.emptyEmailError) getString(R.string.field_is_empty) else null
            binding.passwordTextInput.error = if (it.emptyPasswordError) getString(R.string.field_is_empty) else null

            val isEnableViews = !it.signInInProgress
            binding.emailTextInput.isEnabled = isEnableViews
            binding.passwordTextInput.isEnabled = isEnableViews
            binding.signInButton.isEnabled = isEnableViews
            binding.signUpButton.isEnabled = isEnableViews
            binding.progressBar.visibility = if (it.signInInProgress) View.VISIBLE else View.INVISIBLE
        }


    private fun observeClearPasswordEvent() = viewModel.clearPasswordEvent.observeEvent(viewLifecycleOwner) {
            binding.passwordEditText.text?.clear()
        }

    private fun observeShowErrorToastEvent() = viewModel.showErrorToastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

    private fun observeNavigateToChatList() = viewModel.navigateToChatList.observeEvent(viewLifecycleOwner) {
        findNavController().navigate(R.id.action_singInFragment_to_dialogsFragment) //TODO()
    }

    private fun onSignInPressed() {
        viewModel.singIn(
            email = binding.emailEditText.text.toString(),
            password = binding.passwordEditText.text.toString()
        )
    }

    private fun onSignUpPressed() {
        findNavController().navigate(R.id.action_singInFragment_to_singUpFragment)
    }
}