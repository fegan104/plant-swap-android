package com.frankegan.plantswap.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatePlantPostRequest(
    val title: String,
    val description: String
)
