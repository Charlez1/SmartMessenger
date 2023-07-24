package com.example.smartmessenger.model.source.messages

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class MessagesPagingManager() {

    private var firstLoadedDocument: DocumentSnapshot? = null
    private var lastLoadedDocument: DocumentSnapshot? = null
    private var lastMessageTimestamp: Timestamp? = null
    private var pageIndex: Int = 0

    fun setManagerParams(firstLoadedDocument: DocumentSnapshot?,
                         lastLoadedDocument: DocumentSnapshot?,
                         pageIndex: Int) {
        this.firstLoadedDocument = firstLoadedDocument
        this.lastLoadedDocument = lastLoadedDocument
        this.pageIndex = pageIndex
    }

    fun setLastMessageTimestamp(lastMessageTimestamp: Timestamp?) {
        this.lastMessageTimestamp = lastMessageTimestamp
    }

    fun getLastMessageTimestamp(): Timestamp? {
        return this.lastMessageTimestamp
    }

    fun isFirstLoad(): Boolean = firstLoadedDocument == null

    fun determineLoadDirection(query: Query, pageIndex: Int): Query {
        return if(!isFirstLoad()) {
            if(pageIndex > this.pageIndex)
                query.startAfter(lastLoadedDocument)
            else if(pageIndex < this.pageIndex)
                query.endBefore(firstLoadedDocument)
            else
                query.startAt(firstLoadedDocument)
        } else
            query
    }

    fun removeManagerData() {
        firstLoadedDocument = null
        lastLoadedDocument = null
        lastMessageTimestamp = null
        pageIndex = 0
    }

}