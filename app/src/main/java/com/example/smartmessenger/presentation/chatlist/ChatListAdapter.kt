package com.example.smartmessenger.presentation.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmessenger.databinding.ChatListItemBinding
import com.example.smartmessenger.domain.entity.Chat
import com.example.smartmessenger.utils.DateTimeUtils.formatDateTimeForChatList

interface DialogsListActionListener {

    fun navigateToChat(chatId: String)
}


class ChatListAdapter(
    private val actionListener: DialogsListActionListener
) : PagingDataAdapter<Chat, ChatListAdapter.ChatListViewHolder>(ChatListDiffCallback()), View.OnClickListener {

    override fun onClick(view: View) {
        val dialog = view.tag as Chat
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
        val chat = getItem(position) ?: return
        holder.itemView.tag = chat
        with(holder.binding) {
            nickname.text = chat.anotherMemberUsername
            lastMessage.text = chat.lastMessageText
            timestamp.text = chat.lastMessageTimestamp.formatDateTimeForChatList()
        }
    }

    class ChatListViewHolder(
        val binding: ChatListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}
class ChatListDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.lastMessageTimestamp == newItem.lastMessageTimestamp
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }
}