package com.frankegan.plantswap.data.model

import com.google.firebase.firestore.DocumentReference

data class Conversation(
    val participants: List<Participant>,
    val plantPost: PlantPostId,
    val title: String,
)
