package com.frankegan.plantswap.data.paging

import androidx.paging.PagingSource
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.data.model.UserId
import com.frankegan.plantswap.extensions.toPlantPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

/**
 * A paging data source for fetching plant posts made by the requested user.
 */
class UserPostsPagingSource(
    private val firestore: FirebaseFirestore,
    private val userId: UserId
) : PagingSource<QuerySnapshot, PlantPost>() {

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, PlantPost> {
        return try {
            val currentPage = params.key ?: firestore.collection("plant_posts")
                .whereEqualTo("owner", firestore.document("users/${userId.id}"))
                .limit(PAGE_SIZE)
                .get()
                .await()

            val nextPage = firestore.collection("plant_posts")
                .whereEqualTo("owner", firestore.document("users/$userId"))
                .limit(PAGE_SIZE)
                .startAfter(currentPage.documents.last())
                .get()
                .await()

            val plantPosts = currentPage.documents.map { doc ->
                doc.toPlantPost()
            }

            LoadResult.Page(
                data = plantPosts,
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