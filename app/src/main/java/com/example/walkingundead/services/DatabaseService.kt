package com.example.walkingundead.services

import android.util.Log
import com.example.walkingundead.models.MedicineEntry
import com.google.firebase.database.FirebaseDatabase

class DatabaseService {

    fun addNewMedicineEntry(name: String, type: String, location: String, quantity: Int) {
        val medicineEntry = MedicineEntry(name, type, location, quantity)

        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine")

        dbReference.push().setValue(medicineEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added medicine entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add medicine entry to database", exception)
            }
    }

    fun getAllMedicines(callback: (List<MedicineEntry>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine")

        dbReference.get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val medicines = mutableListOf<MedicineEntry>()
                    for (snapshot in dataSnapshot.children) {
                        val medicineEntry = snapshot.getValue(MedicineEntry::class.java)
                        medicineEntry?.let { medicines.add(it) }
                    }
                    callback(medicines) // Pass the medicines list to the callback
                } else {
                    callback(emptyList()) // Pass an empty list if no medicines are found
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseFetch", "Failed to fetch medicines from database", exception)
                callback(emptyList()) // Return an empty list on failure
            }
    }

    fun editMedicineEntry() {

    }


}