package com.endcodev.roll_dices.presenter.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
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

    fun isConnected() {

        val connectedRef = Firebase.database.getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    Log.d(MainActivity.TAG, "firebase connected")
                    checkVersion()
                } else {
                    Log.d(MainActivity.TAG, "firebase not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(MainActivity.TAG, "firebase listener was cancelled")
            }
        })
    }

    private fun checkVersion() {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("/version")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val needVersion = snapshot.getValue<String>()

                if (needVersion == null) {
                    Log.e(MainActivity.TAG, "versions is null")
                } else {
                    _version.value = needVersion
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(MainActivity.TAG, "Firebase error: $error")
            }
        })
    }
}