package com.endcodev.roll_dices.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endcodev.roll_dices.domain.utils.App
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {

    var isReady: Boolean = false
    private val _version = MutableLiveData<String?>()
    val version: LiveData<String?> get() = _version

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Thread.sleep(500)
            }
            isReady = true
        }
    }

    /**
     * Checks if the app is connected to Firebase
     */
    fun isConnected() {

        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(App.tag, "firebase connected")
                    checkVersion()
                } else {
                    Log.d(App.tag, "firebase not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(App.tag, "firebase listener was cancelled")
            }
        })
    }

    /**
     * Checks the version of the app in Firebase
     */
    private fun checkVersion() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("/version")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val needVersion = snapshot.getValue(String::class.java)

                if (needVersion == null) {
                    Log.e(App.tag, "versions is null")
                } else {
                    _version.value = needVersion
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(App.tag, "Firebase error: $error")
            }
        })
    }
}
