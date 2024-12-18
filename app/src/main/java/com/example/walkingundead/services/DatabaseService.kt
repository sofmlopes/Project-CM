package com.example.walkingundead.services

import android.util.Log
import com.example.walkingundead.models.MedicineEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

    fun getAllMedicines(listener: (List<MedicineEntry>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("medicine")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val medicines = mutableListOf<MedicineEntry>()
                for (data in snapshot.children) {
                    val medicine = data.getValue(MedicineEntry::class.java)
                    val id = data.key
                    if (medicine != null && id != null) {
                        medicine.id = id
                        medicines.add(medicine)
                    }
                }
                listener(medicines)  // Send the updated list to the listener
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching medicines", error.toException())
            }
        })
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