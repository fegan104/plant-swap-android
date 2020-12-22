package com.frankegan.plantswap.data.remote

import com.frankegan.plantswap.data.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


class AuthenticationInterceptor @Inject constructor(
    private val userRepository: UserRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            val user = userRepository.currentUser().first().currentUser
            user?.getIdToken(true)?.await()?.token
        }
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}