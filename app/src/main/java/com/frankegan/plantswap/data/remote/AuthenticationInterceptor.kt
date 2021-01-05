package com.frankegan.plantswap.data.remote

import com.frankegan.plantswap.data.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject


class AuthenticationInterceptor @Inject constructor(
    private val userRepository: UserRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String = runBlocking {
            try {
                val user = userRepository.currentUser().first().getOrNull()
                user?.getIdToken(true)?.await()?.token
            } catch (e: Exception) {
                null
            }
        } ?: return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .body("Invalid Firebase Id-Token".toResponseBody(null))
            .build()

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}