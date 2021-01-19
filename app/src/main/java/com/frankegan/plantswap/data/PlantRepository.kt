package com.frankegan.plantswap.data

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.frankegan.plantswap.data.model.Conversation
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.data.model.UserId
import com.frankegan.plantswap.data.paging.ConversationsPagingSource
import com.frankegan.plantswap.data.paging.UserPostsPagingSource
import com.frankegan.plantswap.data.remote.PlantSwapService
import com.frankegan.plantswap.data.remote.model.CreatePlantPostRequest
import com.frankegan.plantswap.data.remote.model.CreatePlantPostResponse
import com.frankegan.plantswap.di.UploadPhotos
import com.frankegan.plantswap.extensions.getAtLocation
import com.frankegan.plantswap.extensions.km
import com.frankegan.plantswap.extensions.toPlantPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import org.imperiumlabs.geofirestore.GeoFirestore
import javax.inject.Inject

class PlantRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val plantSwapService: PlantSwapService,
    private val uploadPhotos: UploadPhotos
) {

    private val plantsCollection = firestore.collection("plant_posts")
    private val geoFirestore = GeoFirestore(plantsCollection)


    /**
     * Creates a new Plant Posting with the given data.
     */
    suspend fun createPosts(
        postTitle: String,
        postDescription: String,
        photoUris: List<Uri>?,
        currentLocation: GeoPoint?
    ): Result<CreatePlantPostResponse> {
        return runCatching {
            val createdResp = plantSwapService.createPlantPost(
                CreatePlantPostRequest(
                    title = postTitle,
                    description = postDescription
                )
            )
            val doc = firestore.document("/plant_posts/${createdResp.id}")
            if (currentLocation != null) {
                geoFirestore.setLocation(doc.id, currentLocation)
            }
            if (photoUris != null) {
                uploadPhotos(PlantPostId(doc.id), photoUris)
            }

            return@runCatching createdResp
        }
    }

    suspend fun getNearbyPlants(currentLocation: GeoPoint): Result<List<PlantPost>> {
        return geoFirestore.getAtLocation(currentLocation, 100.km).mapCatching { docs ->
            docs.map { doc -> doc.toPlantPost() }
        }
    }

    fun getConversations(userId: UserId): Flow<PagingData<Conversation>> {
        return Pager(
            config = PagingConfig(
                pageSize = ConversationsPagingSource.PAGE_SIZE.toInt(),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ConversationsPagingSource(firestore, userId) }
        ).flow
    }

    fun getUserPlantPosts(userId: UserId): Flow<PagingData<PlantPost>> {
        return Pager(
            config = PagingConfig(
                pageSize = ConversationsPagingSource.PAGE_SIZE.toInt(),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserPostsPagingSource(firestore, userId) }
        ).flow
    }

    suspend fun getPlantPost(plantPostId: PlantPostId): Result<PlantPost> {
        return runCatching {
            plantsCollection.document(plantPostId.id).get().await().toPlantPost()
        }
    }
}