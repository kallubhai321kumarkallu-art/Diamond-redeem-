package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class User(
    @DocumentId val id: String = "",
    val email: String = "",
    val diamondBalance: Long = 0L,
    val coinBalance: Long = 0L
)

data class Offer(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val rewardAmount: Long = 0L
)

data class Submission(
    @DocumentId val id: String = "",
    val userId: String = "",
    val offerId: String = "",
    val screenshotUrl: String = "",
    val status: String = "pending",
    @ServerTimestamp val createdAt: Timestamp? = null
)
