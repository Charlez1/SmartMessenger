package com.example.smartmessenger.screens.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmessenger.R
import com.example.smartmessenger.Singletons
import com.example.smartmessenger.screens.BaseFragment
import com.example.smartmessenger.createViewModel
import com.example.smartmessenger.databinding.FragmentChatListBinding

class ChatListFragment : BaseFragment(R.layout.fragment_chat_list) {


    override val viewModel by createViewModel { ChatListViewModel(Singletons.chatItemsRepository) }

    private lateinit var binding: FragmentChatListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)
        val adapter = DialogsAdapter(viewModel)

        viewModel.dialogsList.observe(requireActivity()) { result ->
            renderSimpleResult(
                root = binding.root,
                result = result,
                onSuccess = { adapter.dialogsList = it }
            )
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter



        return binding.root
    }

}