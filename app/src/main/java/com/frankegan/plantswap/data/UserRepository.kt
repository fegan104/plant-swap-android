package com.frankegan.plantswap.data

import android.util.Log
import com.frankegan.plantswap.data.model.NoUserFoundException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun currentUser(): Flow<Result<FirebaseUser>> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val result = when (val data = firebaseAuth.currentUser) {
                null -> Result.failure(NoUserFoundException())
                else -> Result.success(data)
            }
            offer(result)
        }

        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentLocation(): Result<GeoPoint> = Result.success(GeoPoint(53.2734, -7.77832031))

    suspend fun userSignedIn() {
        currentUser().first().fold(
            onFailure = { Log.e("UserRepo", it.message, it) },
            onSuccess = { user ->
                firestore.document("/users/${user.uid}").set(
                    hashMapOf(
                        "name" to user.displayName
                    ),
                    SetOptions.merge()
                ).await()
            }
        )
    }
}