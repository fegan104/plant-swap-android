package com.frankegan.plantswap.extensions

import com.frankegan.plantswap.data.model.Conversation
import com.frankegan.plantswap.data.model.Participant
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.data.model.UserId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

/**
 * Convert a document snap shot to our application [PlantPost] model. This
 * function throws if all required fields are not present.
 */
fun DocumentSnapshot.toPlantPost(): PlantPost {
    @Suppress("UNCHECKED_CAST")
    return PlantPost(
        active = getBoolean("active")!!,
        description = getString("description")!!,
        geoHash = getString("g"),
        geoPoint = getGeoPoint("l"),
        id = PlantPostId(id),
        owner = UserId(getDocumentReference("owner")!!.id),
        photos = (get("photos") as? List<String>) ?: emptyList(),
        title = getString("title")!!
    )
}

/**
 * Convert a document snap shot to our application [Conversation] model. This
 * function throws if all required fields are not present.
 */
fun DocumentSnapshot.toConversation(): Conversation {
    @Suppress("UNCHECKED_CAST")
    return Conversation(
        participants = parseParticipants(get("users")),
        plantPost = PlantPostId(getDocumentReference("topic_post")!!.id),
        title = getString("title")!!
    )
}

/**
 * Parse nested object array to list of [Participant].
 */
@Suppress("UNCHECKED_CAST")
fun parseParticipants(field: Any?): List<Participant> {
    val maps = field as List<Map<String, Any?>>
    return maps.map { map ->
        val userDocRef = map["user_id"] as DocumentReference
        val name = map["name"] as String
        val photoUrl = map["profile_photo_url"] as String
        Participant(
            userId = UserId(userDocRef.id),
            userName = name,
            profilePhotoUrl = photoUrl
        )
    }
}
