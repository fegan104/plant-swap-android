package com.frankegan.plantswap.data.model

data class Conversation(
    val participants: List<Participant>,
    val plantPost: PlantPostId,
    val title: String,
)
