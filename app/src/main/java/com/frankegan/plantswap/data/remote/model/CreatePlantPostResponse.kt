package com.frankegan.plantswap.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreatePlantPostResponse(val id: String)
