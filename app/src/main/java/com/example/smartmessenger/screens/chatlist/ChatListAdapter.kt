package com.example.smartmessenger.screens.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmessenger.databinding.ChatListItemBinding
import com.example.smartmessenger.model.repositories.entity.ChatItem
import com.example.smartmessenger.model.repositories.entity.Message
import com.example.smartmessenger.screens.currentchat.UsersDiffCallback

interface DialogsListActionListener {

    fun navigateToChat(chatId: String)
}


class ChatListAdapter(
    private val actionListener: DialogsListActionListener
) : PagingDataAdapter<ChatItem, ChatListAdapter.ChatListViewHolder>(ChatListDiffCallback()), View.OnClickListener {

    override fun onClick(view: View) {
        val dialog = view.tag as ChatItem
        actionListener.
        navigateToChat(dialog.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChatListItemBinding.inflate(inflater, parent, false)
        binding.clickableLayout.setOnClickListener(this)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        val dialog = getItem(position) ?: return
        holder.itemView.tag = dialog
        with(holder.binding) {
            nickname.text = dialog.anotherMemberUsername
            lastMessage.text = dialog.lastMessage
            timestamp.text = dialog.timestamp
            countUncheckedMessages.text = dialog.countUncheckedMessages.toString()
        }
    }

    class ChatListViewHolder(
        val binding: ChatListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
class ChatListDiffCallback : DiffUtil.ItemCallback<ChatItem>() {
    override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
        return oldItem == newItem
    }
}