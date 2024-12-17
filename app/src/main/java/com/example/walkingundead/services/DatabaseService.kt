package com.example.walkingundead.services

import android.util.Log
import com.example.walkingundead.models.MedicineEntry
import com.google.firebase.database.FirebaseDatabase

class DatabaseService {

    fun addNewMedicineEntry(name: String, type: String, quantity: Int) {
        val medicineEntry = MedicineEntry(name, type, quantity)

        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine")

        dbReference.push().setValue(medicineEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added medicine entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add medicine entry to database", exception)
            }
    }

    fun getAllMedicines() {

    }

    fun editMedicineEntry() {

    }


}