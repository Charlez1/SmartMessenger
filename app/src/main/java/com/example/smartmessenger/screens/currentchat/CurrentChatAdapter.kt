package com.example.smartmessenger.screens.currentchat

import DateTimeUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmessenger.Singletons
import com.example.smartmessenger.databinding.MessageListItemBinding
import com.example.smartmessenger.databinding.MessageListItemCurrentUserBinding
import com.example.smartmessenger.model.repositories.entity.Message

class CurrentChatAdapter : PagingDataAdapter<Message, RecyclerView.ViewHolder>(UsersDiffCallback()) {

    private val TYPE_LEFT = 0
    private val TYPE_RIGHT = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position) ?: return
        when (holder) {
            is CurrentChatAdapter.MessageListItemViewHolder -> holder.bind(message)
            is CurrentChatAdapter.MessageListItemCurrentUserViewHolder -> holder.bind(message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LEFT -> {
                val binding = MessageListItemBinding.inflate(inflater, parent, false)
                MessageListItemViewHolder(binding)
            }
            else -> {
                val binding = MessageListItemCurrentUserBinding.inflate(inflater, parent, false)
                MessageListItemCurrentUserViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if ((getItem(position) as Message).sender == Singletons.appSettings.getCurrentUId()) {
            TYPE_RIGHT
        } else {
            TYPE_LEFT
        }
    }

    inner class MessageListItemViewHolder(private val binding: MessageListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.timestamp.text = DateTimeUtils().formatDateTimeForCurrentChat(message.timestamp.toLong())
            binding.messageText.text = message.messageText
        }
    }

    inner class MessageListItemCurrentUserViewHolder(private val binding: MessageListItemCurrentUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.timestamp.text = DateTimeUtils().formatDateTimeForCurrentChat(message.timestamp.toLong())
            binding.messageText.text = message.messageText
        }
    }
}

class UsersDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.timestamp == newItem.timestamp
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}