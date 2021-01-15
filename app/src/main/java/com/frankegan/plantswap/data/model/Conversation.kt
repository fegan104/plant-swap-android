package com.frankegan.plantswap.data.model

import com.google.firebase.firestore.DocumentReference

data class Conversation(
    val participants: List<Participant>,
    val plantPost: DocumentReference,
    val title: String,
)
