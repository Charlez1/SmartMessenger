package com.example.smartmessenger.screens.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmessenger.R
import com.example.smartmessenger.Singletons
import com.example.smartmessenger.screens.BaseFragment
import com.example.smartmessenger.databinding.FragmentChatListBinding
import com.example.smartmessenger.screens.createViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatListFragment : BaseFragment(R.layout.fragment_chat_list) {

    override val viewModel by createViewModel { ChatListViewModel(Singletons.chatItemsRepository) }

    private lateinit var binding: FragmentChatListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        val adapter = ChatListAdapter(viewModel)

        lifecycleScope.launch {
            viewModel.dialogsList.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        viewModel.navigateToCurrentChat.observe(viewLifecycleOwner) {
            val value = it.get()
            if (value != null) {
                val direction = ChatListFragmentDirections.actionChatListFragmentToCurrentChatFragment(value.toString())
                findNavController().navigate(direction)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        return binding.root
    }

}