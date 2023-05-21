package com.e.cryptocracy.firebaseDatabase

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.e.cryptocracy.redux.ApplicationState
import com.e.cryptocracy.redux.Store
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FetchFavouriteCoinsFromServer @Inject constructor(
    val store: Store<ApplicationState>,
) : ViewModel() {
    private val db = Firebase.firestore

    fun fetch(user: FirebaseUser?, callback: (status: Boolean) -> Unit) = viewModelScope.launch {
        Log.d("CoinsFromServer", "fetch: ")
        user?.let { myUser ->
            Log.d("CoinsFromServer", "user: ${myUser.uid}")
            db.collection(FirebaseConstant.USER).document(myUser.uid).get().addOnSuccessListener {
                Log.d("CoinsFromServer", "document: $it")
                it?.let { document ->
                    if (document.get("favourite.id") == null) {
                        viewModelScope.launch {
                            store.update { myStore ->
                                return@update myStore.copy(
                                    userLoggedIn = true
                                )
                            }
                        }
                        callback(true)
                        return@addOnSuccessListener
                    }


                    val ids = document.get("favourite.id") as List<String>
                    Log.d("CoinsFromServer", "fetchedIds: $ids")
                    viewModelScope.launch {
                        store.update { myStore ->
                            return@update myStore.copy(
                                favouriteCoinIds = ids.toSet(),
                                userLoggedIn = true
                            )
                        }
                    }
                    callback(true)
                }
            }.addOnFailureListener {
                callback(false)
                Log.d("CoinsFromServer", "addOnFailureListener: " + it.localizedMessage)
            }
        }

    }
}