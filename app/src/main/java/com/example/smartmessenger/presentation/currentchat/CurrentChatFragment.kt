package com.example.smartmessenger.presentation.currentchat

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
import com.example.smartmessenger.data.settings.AppSettings
import com.example.smartmessenger.presentation.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
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

    private fun observeMessageList() = lifecycleScope.launch(Dispatchers.Default) {
        viewModel.messageListFlow.collectLatest { pagingData ->
            adapter.submitData(pagingData)
        }
    }

    private fun observeNewMessageReceived() = viewModel.newMessageReceived.observe(viewLifecycleOwner) {
        adapter.refresh()
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadStates ->
                if (loadStates.refresh is LoadState.NotLoading && isUserAtTopOfList())
                    binding.recyclerView.scrollToPosition(0)
            }
        }
    }
    private fun observeInterlocutorData() = viewModel.interlocutor.observe(viewLifecycleOwner) { interlocutorData ->
        binding.nickname.text = interlocutorData.userName
    }

    private fun onSendButtonClicked() {
        viewModel.sendMessage(args.chatId, binding.messageEditText.text.toString())
        binding.messageEditText.text?.clear()
        val currentPosition = this.layoutManager.findFirstVisibleItemPosition()
        if(currentPosition < 20) {
            binding.recyclerView.smoothScrollToPosition(0)
        } else {
            binding.recyclerView.scrollToPosition(20)
            binding.recyclerView.smoothScrollToPosition(0)
        }
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