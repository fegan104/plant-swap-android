package com.frankegan.plantswap.extensions

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint
import org.imperiumlabs.geofirestore.GeoFirestore
import org.imperiumlabs.geofirestore.extension.getAtLocation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun GeoFirestore.getAtLocation(center: GeoPoint, radius: Kilometers): Result<List<DocumentSnapshot>> {
    return suspendCoroutine { continuation ->
        this.getAtLocation(center, radius.distance) { docs, ex ->
            if (ex != null) {
                continuation.resume(Result.failure(ex))
            } else {
                continuation.resume(Result.success(docs ?: emptyList()))
            }
        }
    }
}