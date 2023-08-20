package com.example.smartmessenger.presentation.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmessenger.R
import com.example.smartmessenger.presentation.BaseFragment
import com.example.smartmessenger.databinding.FragmentChatListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatListFragment : BaseFragment(R.layout.fragment_chat_list) {

    override val viewModel by viewModels<ChatListViewModel>()

    private lateinit var binding: FragmentChatListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        val adapter = ChatListAdapter(viewModel)


        viewModel.loadDialogsList()
        observeProgress()
        observeDialogList(adapter)
        observeNavigateToCurrentChat()

        setUpAdapter(adapter)

        return binding.root
    }

    private fun observeDialogList(adapter: ChatListAdapter) = lifecycleScope.launch(Dispatchers.Default) {
        viewModel.dialogsList.collectLatest { pagingData ->
            adapter.submitData(pagingData)
        }
    }

    private fun observeProgress() = viewModel.loadDialogListInProgress.observe(viewLifecycleOwner) { isVisible ->
        binding.progressBar.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    }

    private fun observeNavigateToCurrentChat() = viewModel.navigateToCurrentChat.observe(viewLifecycleOwner) {
        val value = it.get()
        if (value != null) {
            val direction =
                ChatListFragmentDirections.actionChatListFragmentToCurrentChatFragment(value.toString())
            findNavController().navigate(direction)
        }
    }

    private fun setUpAdapter(adapter: ChatListAdapter) {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
    }

}