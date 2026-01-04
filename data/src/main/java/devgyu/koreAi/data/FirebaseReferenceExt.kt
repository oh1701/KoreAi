package devgyu.koreAi.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query

fun DatabaseReference.getUserReference(adId: String): DatabaseReference = this
    .child("user_adId")
    .child(adId)