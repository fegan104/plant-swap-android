package com.frankegan.plantswap.data.model

import com.google.firebase.firestore.GeoPoint

inline class PlantPostId(val id: String)

data class PlantPost(
    val active: Boolean,
    val description: String,
    val geoHash: String?,
    val geoPoint: GeoPoint?,
    val id: PlantPostId,
    val owner: UserId,
    val photos: List<String>,
    val title: String
)