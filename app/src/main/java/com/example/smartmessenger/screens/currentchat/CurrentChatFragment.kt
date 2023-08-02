package com.example.smartmessenger.screens.currentchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartmessenger.R
import com.example.smartmessenger.databinding.FragmentCurrentChatBinding
import com.example.smartmessenger.model.settings.AppSettings
import com.example.smartmessenger.screens.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CurrentChatFragment : BaseFragment(R.layout.fragment_current_chat) {

    private val args by navArgs<CurrentChatFragmentArgs>()
    override val viewModel by viewModels<CurrentChatViewModel>()
    private lateinit var binding: FragmentCurrentChatBinding
    private lateinit var adapter: CurrentChatAdapter
    private lateinit var layoutManager: LinearLayoutManager
    @Inject lateinit var appSettings: AppSettings
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCurrentChatBinding.inflate(inflater, container, false)
        adapter = CurrentChatAdapter(appSettings)

        viewModel.getMessages(args.chatId)
        observeNewMessageReceived()
        observeMessageList()

        viewModel.getInterlocutorData(args.chatId)
        observeInterlocutorData()

        binding.sendButton.setOnClickListener { onSendButtonClicked() }
        binding.backButton.setOnClickListener { onBackButtonClicked() }
        setUpAdapter()

        return binding.root
    }

    private fun observeMessageList() = viewModel.messageList.observe(viewLifecycleOwner) { messageList ->
        adapter.submitData(lifecycle, messageList)
    }

    private fun observeNewMessageReceived() = viewModel.messageReceived.observe(viewLifecycleOwner) {
        viewModel.removeStartAfterDocumentValue()
        viewModel.setLastMessage(args.chatId, it)
        adapter.refresh()

        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                if (loadStates.refresh is LoadState.NotLoading && isUserAtTopOfList())
                    binding.recyclerView.scrollToPosition(0)
            }
        }
    }
    private fun observeInterlocutorData() = viewModel.interlocutorData.observe(viewLifecycleOwner) { interlocutorData ->
        binding.nickname.text = interlocutorData.nickname
    }

    private fun onSendButtonClicked() {
        viewModel.sendMessage(args.chatId, binding.messageEditText.text.toString())
        binding.messageEditText.text?.clear()
        binding.recyclerView.scrollToPosition(0)
    }
    private fun onBackButtonClicked() {
        findNavController().popBackStack()
    }

    private fun setUpAdapter() {
        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.reverseLayout = true
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

    }

    private fun isUserAtTopOfList() : Boolean {
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        return firstVisibleItemPosition == 0
    }

}