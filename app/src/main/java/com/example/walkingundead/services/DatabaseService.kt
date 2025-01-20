package com.example.walkingundead.services

import android.util.Log
import com.example.walkingundead.models.Food
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.ReportZombie
import com.example.walkingundead.models.Shelter
import com.example.walkingundead.models.Skill
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class DatabaseService {

    fun addNewSkill(name: String) {
        val skill = Skill(
            name = name,
            emailRegisteredBy = Firebase.auth.currentUser?.email?: "Unknown"
        )

        val dbReference = FirebaseDatabase.getInstance().reference.child("Skills")

        dbReference.push().setValue(skill)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added skill entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add skill entry to database", exception)
            }
    }

    fun getAllSkills(listener: (List<Skill>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Skills")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val skills = mutableListOf<Skill>()
                for (data in snapshot.children) {
                    val skill = data.getValue(Skill::class.java)
                    val id = data.key
                    if (skill != null && id != null) {
                        skill.id = id
                        skills.add(skill)
                    }
                }
                listener(skills)  // Send the updated list to the listener
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching skills", error.toException())
            }
        })
    }

    fun editSkill(id: String, updatedEntry: Skill) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Skills").child(id)

        dbReference.setValue(updatedEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpdate", "Successfully updated skill entry")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpdate", "Failed to update skill entry", exception)
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

        val dbReference = FirebaseDatabase.getInstance().reference.child("Medicines")

        dbReference.push().setValue(medicineEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added medicine entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add medicine entry to database", exception)
            }
    }

    fun getAllMedicines(listener: (List<MedicineEntry>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Medicines")

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
        val dbReference = FirebaseDatabase.getInstance().reference.child("Medicines").child(id)

        dbReference.setValue(updatedEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpdate", "Successfully updated medicine entry")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpdate", "Failed to update medicine entry", exception)
            }
    }

    fun deleteMedicineEntry(id: String) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Medicines").child(id)

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

        val dbReference = FirebaseDatabase.getInstance().reference.child("Shelters")

        dbReference.push().setValue(shelter)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added shelter entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add shelter entry to database", exception)
            }
    }

    fun getAllShelters(listener: (List<Shelter>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Shelters")

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
                Log.e("FirebaseError", "Error fetching shelters", error.toException())
            }
        })
    }

    fun addNewReportZombie(location: String) {
        val reportZombie = ReportZombie(
            location = location,
            emailRegisteredBy = Firebase.auth.currentUser?.email?: "Unknown"
        )

        val dbReference = FirebaseDatabase.getInstance().reference.child("Report Zombie")

        dbReference.push().setValue(reportZombie)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added report zombie to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add report zombie to database", exception)
            }
    }

    fun getAllZombies(listener: (List<ReportZombie>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Report Zombie")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reportsZombies = mutableListOf<ReportZombie>()
                for (data in snapshot.children) {
                    val zombie = data.getValue(ReportZombie::class.java)
                    val id = data.key
                    if (zombie != null && id != null) {
                        zombie.id = id
                        reportsZombies.add(zombie)
                    }
                }
                listener(reportsZombies)  // Send the updated list to the listener
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching zombies", error.toException())
            }
        })

    }

    fun addNewFoodEntry(name: String, type: String, location: String, quantity: Int) {
        val foodEntry = Food(
            name = name,
            type = type,
            location = location,
            quantity = quantity,
            emailRegisteredBy = Firebase.auth.currentUser?.email?: "Unknown"
        )

        val dbReference = FirebaseDatabase.getInstance().reference.child("Food")

        dbReference.push().setValue(foodEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added food entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add food entry to database", exception)
            }
    }

    fun getAllFoods(listener: (List<Food>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Foods")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val foods = mutableListOf<Food>()
                for (data in snapshot.children) {
                    val food = data.getValue(Food::class.java)
                    val id = data.key
                    if (food != null && id != null) {
                        food.id = id
                        foods.add(food)
                    }
                }
                listener(foods)  // Send the updated list to the listener
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching foods", error.toException())
            }
        })
    }

    fun editFoodEntry(id: String, updatedEntry: Food) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Foods").child(id)

        dbReference.setValue(updatedEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpdate", "Successfully updated food entry")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpdate", "Failed to update food entry", exception)
            }
    }

    fun deleteFoodEntry(id: String) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Foods").child(id)

        dbReference.removeValue()
            .addOnSuccessListener {
                Log.d("FirebaseDelete", "Successfully deleted food entry with ID: $id")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseDelete", "Failed to delete food entry with ID: $id", exception)
            }
    }
}