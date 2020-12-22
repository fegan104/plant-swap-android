package com.frankegan.plantswap.data

import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.data.remote.PlantSwapService
import com.frankegan.plantswap.data.remote.model.CreatePlantPostRequest
import com.frankegan.plantswap.data.remote.model.CreatePlantPostResponse
import com.frankegan.plantswap.extensions.getAtLocation
import com.frankegan.plantswap.extensions.km
import com.frankegan.plantswap.extensions.toPlantPost
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import org.imperiumlabs.geofirestore.GeoFirestore
import javax.inject.Inject

class PlantRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val plantSwapService: PlantSwapService
) {

    private val plantsCollection = firestore.collection("plant_posts")
    private val geoFirestore = GeoFirestore(plantsCollection)


    /**
     * Creates a new Plant Posting with the given data.
     */
    suspend fun createPosts(postTitle: String, postDescription: String, currentLocation: GeoPoint): Result<CreatePlantPostResponse> {
        return runCatching {
            val createdResp = plantSwapService.createPlantPost(
                CreatePlantPostRequest(
                    title = postTitle,
                    description = postDescription
                )
            )
            val docId = firestore.document("/plant_posts/${createdResp.id}").id
            geoFirestore.setLocation(docId, currentLocation)
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
    }

    suspend fun getUser() = runCatching {
        plantSwapService.getUser()
    }
}