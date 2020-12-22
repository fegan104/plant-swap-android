package com.frankegan.plantswap.data.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.GeoPoint
import org.imperiumlabs.geofirestore.core.GeoHash

data class PlantPost(
    val active: Boolean,
    val description: String,
    val geoHash: String?,
    val geoPoint: GeoPoint?,
    val id: String,
    val owner: DocumentReference,
    val photos: List<String>,
    val title: String
)