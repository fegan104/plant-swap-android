package com.frankegan.plantswap.di

import android.net.Uri
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.data.remote.PlantSwapService
import com.frankegan.plantswap.data.remote.model.GalleryPostRequest
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

private val photoUrls = listOf(
    "https://images.unsplash.com/photo-1453474473052-08cd150dfe87",
    "https://images.unsplash.com/photo-1509223197845-458d87318791",
    "https://images.unsplash.com/photo-1509587584298-0f3b3a3a1797",
    "https://images.unsplash.com/photo-1511689774932-3aca18459e68"
)

class UploadPhotos @Inject constructor(
    private val plantSwapService: PlantSwapService
) {

    suspend operator fun invoke(
        plantPostId: PlantPostId,
        photoUris: List<Uri>
    ) {
        coroutineScope {
            plantSwapService.addPhotosToGallery(plantPostId.id, GalleryPostRequest(photoUrls))
        }
    }
}