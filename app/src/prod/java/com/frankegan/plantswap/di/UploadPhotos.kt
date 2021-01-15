package com.frankegan.plantswap.di

import android.net.Uri
import android.util.Log
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.data.remote.PlantSwapService
import com.frankegan.plantswap.data.remote.model.GalleryPostRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UploadPhotos @Inject constructor(
    private val storage: FirebaseStorage,
    private val plantSwapService: PlantSwapService
) {

    suspend operator fun invoke(
        plantPostId: PlantPostId,
        photoUris: List<Uri>
    ) {
        coroutineScope {
            val downloadUrls = photoUris
                .map { uri ->
                    async(Dispatchers.IO) {
                        Log.d("PlantRepository", "putting file: $uri")
                        val uploadTask = storage.getReference("/posts/$plantPostId/${UUID.randomUUID()}").putFile(uri).await()
                        uploadTask.metadata?.reference?.downloadUrl?.await()?.toString()
                    }
                }
                .toList()
                .awaitAll()
                .filterNotNull()

            plantSwapService.addPhotosToGallery(plantPostId.id, GalleryPostRequest(downloadUrls))
        }
    }
}