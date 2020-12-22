package com.frankegan.plantswap.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlantSwapUser(
    val uid: String,
    val email: String,
    val emailVerified: Boolean
)