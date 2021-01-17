package com.frankegan.plantswap.extensions

import com.frankegan.plantswap.data.model.Conversation
import com.frankegan.plantswap.data.model.Participant
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.data.model.UserId
import com.google.firebase.firestore.DocumentSnapshot


fun DocumentSnapshot.toPlantPost(): PlantPost? {
    @Suppress("UNCHECKED_CAST")
    return PlantPost(
        active = getBoolean("active") ?: return null,
        description = getString("description") ?: return null,
        geoHash = getString("g"),
        geoPoint = getGeoPoint("l"),
        id = PlantPostId(id),
        owner = UserId(getDocumentReference("owner")?.id ?: return null),
        photos = (get("photos") as? List<String>) ?: emptyList(),
        title = getString("description") ?: return null
    )
}

fun DocumentSnapshot.toConversation(): Conversation? {
    @Suppress("UNCHECKED_CAST")
    return Conversation(
        participants = (get("participants") as? List<Participant>) ?: emptyList(),
        plantPost = PlantPostId(getDocumentReference("plantPost")?.id ?: return null),
        title = getString("title") ?: ""
    )
}
