package com.om_tat_sat.grade_ace.valueSaverNewUi

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.om_tat_sat.grade_ace.valueSaverNewUi.firebaseSingleton.dataSnapshot

object firebasePyqLoader {
    var dataSnapshot: DataSnapshot? =null
    fun fetchPYQ(firebaseAuth: FirebaseAuth, databaseReference: DatabaseReference, callback: (DataSnapshot?) -> Unit) {
        if (dataSnapshot != null) {
            callback(dataSnapshot)
            return
        }
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                Log.e("FirebaseDataManager", "onDataChange: ${snapshot.value}")
                dataSnapshot = snapshot
                callback(snapshot)
            }
            override fun onCancelled(error: DatabaseError) {
//                Log.e("FirebaseDataManager", "onCancelled: ${error.message}")
                callback(null)
            }
        })
    }
}