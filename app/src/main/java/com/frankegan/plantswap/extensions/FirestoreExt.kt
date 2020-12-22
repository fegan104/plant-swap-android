package com.frankegan.plantswap.extensions

import com.frankegan.plantswap.data.model.PlantPost
import com.google.firebase.firestore.DocumentSnapshot


fun DocumentSnapshot.toPlantPost(): PlantPost? {
    @Suppress("UNCHECKED_CAST")
    return PlantPost(
        active = getBoolean("active") ?: return null,
        description = getString("description") ?: return null,
        geoHash = getString("g"),
        geoPoint = getGeoPoint("l"),
        id = id,
        owner = getDocumentReference("owner") ?: return null,
        photos = (get("photos") as? List<String>) ?: emptyList(),
        title = getString("description") ?: return null
    )
}