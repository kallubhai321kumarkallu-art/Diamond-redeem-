package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.*
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AppRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth = Firebase.auth

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeUser(): Flow<User?> = flow {
        val uid = requireUserId()
        emitAll(
            db.collection("users").document(uid)
                .snapshots()
                .map { snapshot -> snapshot.toObject(User::class.java) }
        )
    }

    fun observeOffers(): Flow<List<Offer>> = flow {
        emitAll(
            db.collection("offers")
                .snapshots()
                .map { snapshot -> snapshot.toObjects(Offer::class.java) }
        )
    }

    suspend fun submitOffer(offerId: String, screenshotUrl: String) {
        val uid = requireUserId()
        val docRef = db.collection("submissions").document()
        val payload = mapOf(
            "userId" to uid,
            "offerId" to offerId,
            "screenshotUrl" to screenshotUrl,
            "status" to "pending",
            "createdAt" to FieldValue.serverTimestamp()
        )
        docRef.set(payload).await()
    }
}
