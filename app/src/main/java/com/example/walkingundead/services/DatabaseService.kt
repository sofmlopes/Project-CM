package com.example.walkingundead.services

import android.util.Log
import com.example.walkingundead.models.MedicineEntry
import com.google.firebase.database.FirebaseDatabase

class DatabaseService {

    fun addNewMedicineEntry(name: String, type: String, location: String, quantity: Int) {
        val medicineEntry = MedicineEntry(
            name = name,
            type = type,
            location = location,
            quantity = quantity,
        )

        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine")

        dbReference.push().setValue(medicineEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added medicine entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add medicine entry to database", exception)
            }
    }

    fun getAllMedicines(onComplete: (List<MedicineEntry>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine")

        dbReference.get()
            .addOnSuccessListener { snapshot ->
                val medicines = snapshot.children.mapNotNull { child ->
                    val medicineEntry = child.getValue(MedicineEntry::class.java)
                    val id = child.key // Get the unique ID
                    if (medicineEntry != null && id != null) {
                        medicineEntry.id = id // Assign the ID to the model
                        medicineEntry // Add the updated object to the list
                    } else {
                        null // Skip invalid data
                    }
                }
                onComplete(medicines) // Pass the list to the callback
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseFetch", "Failed to fetch medicines", exception)
                onComplete(emptyList()) // Pass an empty list on failure
            }
    }

    fun editMedicineEntry(id: String, updatedEntry: MedicineEntry) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine").child(id)

        dbReference.setValue(updatedEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpdate", "Successfully updated medicine entry")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpdate", "Failed to update medicine entry", exception)
            }
    }

    fun deleteMedicineEntry(id: String) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine").child(id)

        dbReference.removeValue()
            .addOnSuccessListener {
                Log.d("FirebaseDelete", "Successfully deleted medicine entry with ID: $id")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseDelete", "Failed to delete medicine entry with ID: $id", exception)
            }
    }
}