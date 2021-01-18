package com.frankegan.plantswap.extensions

import com.frankegan.plantswap.data.model.Conversation
import com.frankegan.plantswap.data.model.Participant
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.data.model.PlantPostId
import com.frankegan.plantswap.data.model.UserId
import com.google.firebase.firestore.DocumentReference
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

fun DocumentSnapshot.toConversation(): Conversation {
    @Suppress("UNCHECKED_CAST")
    return Conversation(
        participants = parseParticipants(get("users")),
        plantPost = PlantPostId(getDocumentReference("topicPost")!!.id),
        title = getString("title")!!
    )
}

@Suppress("UNCHECKED_CAST")
fun parseParticipants(field: Any?): List<Participant> {
    val maps = field as List<Map<String, Any?>>
    return maps.map { map ->
        val userDocRef = map["userId"] as DocumentReference
        val name = map["name"] as String
        val photoUrl = map["profilePhotoUrl"] as String
        Participant(
            userId = UserId(userDocRef.id),
            userName = name,
            profilePhotoUrl = photoUrl
        )
    }
}
