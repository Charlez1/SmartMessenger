package com.example.smartmessenger.authentication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.smartmessenger.*
import com.example.smartmessenger.base.BaseFragment
import com.example.smartmessenger.databinding.FragmentSingUpBinding

class SingUpFragment : BaseFragment(R.layout.fragment_sing_up) {

    override val viewModel by createViewModel { SingUpViewModel(Singletons.accountsRepository) }
    private lateinit var binding: FragmentSingUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSingUpBinding.inflate(inflater, container, false)

        binding.createAccountButton.setOnClickListener { createAccountPressed() }

        viewModel.state.observe(viewLifecycleOwner) {
            binding.emailTextInput.error = if (it.emptyEmailError) getString(R.string.field_is_empty) else null
            binding.passwordTextInput.error = if (it.emptyPasswordError) getString(R.string.field_is_empty) else null
            binding.repeatPasswordTextInput.error = if (it.emptyPasswordRepeatError) getString(R.string.field_is_empty) else null

            val isEnableViews = !it.signUpInProgress
            binding.emailTextInput.isEnabled = isEnableViews
            binding.passwordTextInput.isEnabled = isEnableViews
            binding.repeatPasswordTextInput.isEnabled = isEnableViews
            binding.progressBar.visibility = if (it.signUpInProgress) View.VISIBLE else View.INVISIBLE
        }

        viewModel.showErrorToastEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.navigateToSingIn.observeEvent(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun createAccountPressed() {
        viewModel.singUp(
            SignUpData(
                email = binding.emailEditText.text.toString(),
                password = binding.passwordEditText.text.toString(),
                repeatPassword = binding.repeatPasswordEditText.text.toString()
            )
        )
    }
}