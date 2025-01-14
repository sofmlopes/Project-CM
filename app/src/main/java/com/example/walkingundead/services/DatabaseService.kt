package com.example.walkingundead.services

import android.util.Log
import com.example.walkingundead.R
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.Shelter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class DatabaseService {


    // Function to get the current user's ID (assumes Firebase Authentication is used)
    fun getUserId(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid
    }

    // Function to save selected skills to Firebase Realtime Database
    fun saveSkillsToFirebase(selectedSkills: List<String>, userId: String) {
        val database = FirebaseDatabase.getInstance()
        val userSkillsRef = database.getReference("users/$userId/selectedSkills")

        // Save the selected skills to the Realtime Database
        userSkillsRef.setValue(selectedSkills)
            .addOnSuccessListener {
                // Successfully saved the skills
                Log.d("RealtimeDatabase", "Skills saved successfully")
            }
            .addOnFailureListener { e ->
                // Error occurred while saving the skills
                Log.w("RealtimeDatabase", "Error saving skills", e)
            }
    }

    // Function to retrieve selected skills from Firebase Realtime Database
    fun getSkillsFromFirebase(userId: String, onSkillsLoaded: (List<String>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userSkillsRef = database.getReference("users/$userId/selectedSkills")

        // Retrieve the selected skills from Realtime Database
        userSkillsRef.get()
            .addOnSuccessListener { snapshot ->
                val skills = snapshot.getValue(List::class.java) as? List<String> ?: emptyList()
                onSkillsLoaded(skills)
            }
            .addOnFailureListener { e ->
                // Error occurred while retrieving the skills
                Log.w("RealtimeDatabase", "Error getting skills", e)
                onSkillsLoaded(emptyList())
            }
    }

    fun addNewMedicineEntry(name: String, type: String, location: String, quantity: Int) {
        val medicineEntry = MedicineEntry(
            name = name,
            type = type,
            location = location,
            quantity = quantity,
            emailRegisteredBy = Firebase.auth.currentUser?.email?: "Unknown"
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

    fun addNewShelter(name: String, location: String, nrOfBeds: Int, occupiedBeds: Int) {
        val shelter = Shelter(
            name = name,
            location = location,
            numberOfBeds = nrOfBeds,
            occupiedBeds = occupiedBeds,
            emailRegisteredBy = Firebase.auth.currentUser?.email?: "Unknown"
        )

        val dbReference = FirebaseDatabase.getInstance().reference.child("shelter")

        dbReference.push().setValue(shelter)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added shelter entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add shelter entry to database", exception)
            }
    }

    fun getAllShelters(listener: (List<Shelter>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("shelter")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val shelters = mutableListOf<Shelter>()
                for (data in snapshot.children) {
                    val shelter = data.getValue(Shelter::class.java)
                    val id = data.key
                    if (shelter != null && id != null) {
                        shelter.id = id
                        shelters.add(shelter)
                    }
                }
                listener(shelters)  // Send the updated list to the listener
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching medicines", error.toException())
            }
        })
    }
}