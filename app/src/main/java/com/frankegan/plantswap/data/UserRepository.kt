package com.frankegan.plantswap.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    fun currentUser(): Flow<FirebaseAuth> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            offer(firebaseAuth)
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    fun getCurrentLocation(): Result<GeoPoint> = Result.success(GeoPoint(53.2734, -7.77832031))
}