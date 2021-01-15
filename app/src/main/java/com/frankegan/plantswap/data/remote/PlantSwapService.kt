package com.frankegan.plantswap.data.remote

import com.frankegan.plantswap.data.remote.model.ConversationRequest
import com.frankegan.plantswap.data.remote.model.CreatePlantPostRequest
import com.frankegan.plantswap.data.remote.model.CreatePlantPostResponse
import com.frankegan.plantswap.data.remote.model.GalleryPostRequest
import com.frankegan.plantswap.data.remote.model.PlantSwapUser
import com.squareup.okhttp.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


interface PlantSwapService {

    @POST("plant_posts")
    suspend fun createPlantPost(@Body createPlantPostRequest: CreatePlantPostRequest): CreatePlantPostResponse

    @PATCH("plant_posts/{post_id}/close")
    suspend fun closePlantPost(@Path("post_id") plantPostId: String)

    @POST("plant_posts/{post_id}/gallery")
    suspend fun addPhotosToGallery(@Path("post_id") plantPostId: String, @Body photoUrls: GalleryPostRequest)

    @DELETE("plant_posts/{post_id}/gallery/{photo_id}")
    suspend fun deletePhotosFromGallery(@Path("post_id") plantPostId: String, @Path("photo_id") photoUrl: String)

    @POST("conversations")
    suspend fun createConversation(@Body conversationRequest: ConversationRequest)

    @GET("users")
    suspend fun getUser(): PlantSwapUser
}