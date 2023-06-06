package com.example.smartmessenger.screens.chatlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartmessenger.databinding.ChatListItemBinding
import com.example.smartmessenger.model.repositories.entity.ChatItem

interface DialogsListActionListener {

    fun navigateToChat(chatId: String)
}


class DialogsAdapter(
    private val actionListener: DialogsListActionListener
) : RecyclerView.Adapter<DialogsAdapter.DialogsViewHolder>(), View.OnClickListener {

    var dialogsList: List<ChatItem> = emptyList()

    override fun onClick(view: View) {
        val dialog = view.tag as ChatItem
        actionListener.navigateToChat(dialog.id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChatListItemBinding.inflate(inflater, parent, false)
        binding.clickableLayout.setOnClickListener(this)
        return DialogsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DialogsViewHolder, position: Int) {
        val dialog = dialogsList[position]
        holder.itemView.tag = dialog
        with(holder.binding) {
            nickname.text = dialog.anotherMember
            lastMessage.text = dialog.lastMessage
            timestamp.text = "123"
        }
    }

    override fun getItemCount(): Int {
        return dialogsList.size
    }


    class DialogsViewHolder(
        val binding: ChatListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}