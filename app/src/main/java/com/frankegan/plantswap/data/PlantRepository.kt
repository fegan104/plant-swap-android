package com.frankegan.plantswap.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.data.remote.PlantSwapService
import com.frankegan.plantswap.data.remote.model.CreatePlantPostRequest
import com.frankegan.plantswap.data.remote.model.CreatePlantPostResponse
import com.frankegan.plantswap.data.remote.model.GalleryPostRequest
import com.frankegan.plantswap.di.UploadPhotos
import com.frankegan.plantswap.extensions.getAtLocation
import com.frankegan.plantswap.extensions.km
import com.frankegan.plantswap.extensions.toPlantPost
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import org.imperiumlabs.geofirestore.GeoFirestore
import java.util.UUID
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
        return geoFirestore.getAtLocation(currentLocation, 100.km).map { docs ->
            docs.mapNotNull { doc -> doc.toPlantPost() }
        }
    }

    fun getConversations(userId: String): Query {
        return firestore
            .collectionGroup("participants")
            .whereEqualTo("participant", firestore.document("users/$userId"))
    }

    fun getPlantPosts(userId: String): Query {
        return firestore
            .collection("plant_posts")
            .whereEqualTo("owner", firestore.document("users/$userId"))
            .startAt(100)
    }
}