package com.frankegan.plantswap.data.paging

import androidx.paging.PagingSource
import com.frankegan.plantswap.data.model.Conversation
import com.frankegan.plantswap.data.model.UserId
import com.frankegan.plantswap.extensions.toConversation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

private const val STARTING_PAGE_INDEX = 0L

class ConversationsPagingSource(
    private val firestore: FirebaseFirestore,
    private val userId: UserId
) : PagingSource<QuerySnapshot, Conversation>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Conversation> {
        return try {
            // Step 1
            val currentPage = params.key ?: firestore.collectionGroup("participants")
                .whereEqualTo("participant", firestore.document("users/$userId"))
                .limit(PAGE_SIZE)
                .get()
                .await()

            // Step 2
            val lastDocumentSnapshot = currentPage.documents.last()

            // Step 3
            val nextPage = firestore.collectionGroup("participants")
                .whereEqualTo("participant", firestore.document("users/$userId"))
                .limit(PAGE_SIZE)
                .startAfter(lastDocumentSnapshot)
                .get()
                .await()

            // Step 4
            LoadResult.Page(
                data = currentPage.documents.mapNotNull { it.toConversation() },
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