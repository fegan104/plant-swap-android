package com.frankegan.plantswap.data.paging

import androidx.paging.PagingSource
import com.frankegan.plantswap.data.model.Conversation
import com.frankegan.plantswap.data.model.UserId
import com.frankegan.plantswap.extensions.toConversation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class ConversationsPagingSource(
    private val firestore: FirebaseFirestore,
    private val userId: UserId
) : PagingSource<QuerySnapshot, Conversation>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Conversation> {
        return try {
            val currentPage = params.key ?: firestore.collectionGroup("participants")
                .whereEqualTo("participant", firestore.document("users/${userId.id}"))
                .limit(PAGE_SIZE)
                .get()
                .await()

            val nextPage = firestore.collectionGroup("participants")
                .whereEqualTo("participant", firestore.document("users/$userId"))
                .limit(PAGE_SIZE)
                .startAfter(currentPage.documents.last())
                .get()
                .await()

            val conversations = currentPage.documents
                .mapNotNull { it.reference.parent.parent!!.get().await() }
                .map { it.toConversation() }

            LoadResult.Page(
                data = conversations,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {

        const val PAGE_SIZE = 20L
    }
}