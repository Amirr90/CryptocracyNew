package com.e.cryptocracy.firebaseDatabase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UpdateFavouriteIds @Inject constructor(
) {
    val TAG = "UpdateFavouriteIds"
    private val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val map = HashMap<String, Any>()
    fun update(coinId: String, add: Boolean = true) {
        Log.d(TAG, "update: $coinId")
        auth.currentUser?.apply {
            Log.d(TAG, "UId: " + this.uid)
            map["favourite.id"] =
                if (add) FieldValue.arrayUnion(coinId) else FieldValue.arrayRemove(coinId)
            val uid = this.uid
            db.collection(FirebaseConstant.USER).document(uid).update(map)
                .addOnFailureListener {
                    Log.d(TAG, "addOnFailureListener: " + it.localizedMessage)
                }

        }

    }

}