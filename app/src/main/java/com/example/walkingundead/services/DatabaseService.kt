package com.example.walkingundead.services

import android.util.Log
import com.example.walkingundead.models.Contact
import com.example.walkingundead.models.Food
import com.example.walkingundead.models.MedicineEntry
import com.example.walkingundead.models.Profile
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
    //Profile
    fun addNewProfileEntry(name: String, email: String, skills: MutableList<Skill>) {
        val profileEntry = Profile(
            email = email,
            skills = skills
        )

        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.push().setValue(profileEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpload", "Added profile entry to database")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpload", "Failed to add profile entry to database", exception)
            }
    }

    fun getAllProfiles(listener: (List<Profile>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profiles = mutableListOf<Profile>()
                for (data in snapshot.children) {
                    val profile = data.getValue(Profile::class.java)
                    val id = data.key?.toIntOrNull()
                    if (profile != null) {
                        profile.id = id
                        profiles.add(profile)
                    }
                }
                listener(profiles) // Send the updated list to the listener
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching profiles", error.toException())
            }
        })
    }

    fun editProfileEntry(id: Int, updatedEntry: Profile) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles").child(id.toString())

        dbReference.setValue(updatedEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpdate", "Successfully updated profile entry")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpdate", "Failed to update profile entry", exception)
            }
    }

    //Skills
    fun getProfileSkills(email: String, listener: (List<Skill>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val skills = mutableListOf<Skill>()
                    for (data in snapshot.children) {
                        val profile = data.getValue(Profile::class.java)
                        if (profile != null && profile.skills.isNotEmpty()) {
                            skills.addAll(profile.skills!!)
                        }
                    }
                    listener(skills) // Send the updated list of skills to the listener
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching skills for profile with email $email", error.toException())
                }
            })
    }

    fun addSkillToProfile(email: String, skill: Skill) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val profile = data.getValue(Profile::class.java)
                            val profileKey = data.key
                            if (profile != null && profileKey != null) {
                                val updatedSkills = profile.skills!!.toMutableList()
                                updatedSkills.add(skill)
                                val updatedProfile = profile.copy(skills = updatedSkills)

                                // Update the profile in the database
                                dbReference.child(profileKey).setValue(updatedProfile)
                                    .addOnSuccessListener {
                                        Log.d("FirebaseUpdate", "Successfully added skill to profile")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("FirebaseUpdate", "Failed to add skill to profile", exception)
                                    }
                                return // Exit loop after updating the first matching profile
                            }
                        }
                    } else {
                        Log.e("FirebaseError", "No profile found with email $email")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error querying profile with email $email", error.toException())
                }
            })
    }

    fun removeSkillFromProfile(email: String, skill: Skill) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val profile = data.getValue(Profile::class.java)
                            val profileKey = data.key
                            if (profile != null && profileKey != null) {
                                // Remove the skill from the skills list
                                val updatedSkills = profile.skills.toMutableList()
                                updatedSkills.removeIf { it.name == skill.name }

                                // Update the profile with the new list of skills
                                val updatedProfile = profile.copy(skills = updatedSkills)

                                // Update the profile in the database
                                dbReference.child(profileKey).setValue(updatedProfile)
                                    .addOnSuccessListener {
                                        Log.d("FirebaseUpdate", "Successfully removed skill from profile")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("FirebaseUpdate", "Failed to remove skill from profile", exception)
                                    }
                                return // Exit loop after updating the first matching profile
                            }
                        }
                    } else {
                        Log.e("FirebaseError", "No profile found with email $email")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error querying profile with email $email", error.toException())
                }
            })
    }

    //Contacts
    fun getProfileContacts(email: String, listener: (List<Contact>) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contacts = mutableListOf<Contact>()
                    for (data in snapshot.children) {
                        val profile = data.getValue(Profile::class.java)
                        if (profile != null) {
                            contacts.addAll(profile.contacts)
                        }
                    }
                    listener(contacts) // Send the updated list of skills to the listener
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error fetching contacts for profile with email $email", error.toException())
                }
            })
    }

    fun getContactsByEmail(email: String, listener: (List<Contact>?) -> Unit) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profiles = snapshot.children.mapNotNull { data ->
                    val profile = data.getValue(Profile::class.java)
                    profile?.apply { id = data.key?.toIntOrNull() }
                }

                // Find the profile with the matching email
                val matchingProfile = profiles.find { it.email == email }
                listener(matchingProfile?.contacts?.toList())
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching profiles", error.toException())
                listener(null) // Return null if there's an error
            }
        })
    }

    fun addContactToProfile(email: String, contact: Contact) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {
                            val profile = data.getValue(Profile::class.java)
                            val profileKey = data.key
                            if (profile != null && profileKey != null) {
                                val updatedContacts = profile.contacts.toMutableList()
                                updatedContacts.add(contact)
                                val updatedProfile = profile.copy(contacts = updatedContacts)

                                // Update the profile in the database
                                dbReference.child(profileKey).setValue(updatedProfile)
                                    .addOnSuccessListener {
                                        Log.d("FirebaseUpdate", "Successfully added contact to profile")
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("FirebaseUpdate", "Failed to add contact to profile", exception)
                                    }
                                return // Exit loop after updating the first matching profile
                            }
                        }
                    } else {
                        Log.e("FirebaseError", "No profile found with email $email")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error querying profile with email $email", error.toException())
                }
            })
    }

    fun removeContact(email: String, contactToRemove: Contact) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Profiles")

        dbReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val profile = data.getValue(Profile::class.java)
                        if (profile != null) {
                            val contacts = profile.contacts.toMutableList()
                            if (contacts.remove(contactToRemove)) {
                                // Update the contacts in Firebase
                                data.ref.child("contacts").setValue(contacts)
                            }
                            return
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseError", "Error removing contact for profile with email $email", error.toException())
                }
            })
    }

    //Medicine
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

    //Shelter
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

    fun editShelter(id: String, updatedEntry: Shelter) {
        val dbReference = FirebaseDatabase.getInstance().reference.child("Shelters").child(id)

        dbReference.setValue(updatedEntry)
            .addOnSuccessListener {
                Log.d("FirebaseUpdate", "Successfully updated shelter entry")
            }
            .addOnFailureListener { exception ->
                Log.e("FirebaseUpdate", "Failed to update shelter entry", exception)
            }
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

    //Food
    fun addNewFoodEntry(name: String, type: String, location: String, quantity: Int, expirationDate : String) {
        val foodEntry = Food(
            name = name,
            type = type,
            location = location,
            quantity = quantity,
            expirationDate = expirationDate,
            emailRegisteredBy = Firebase.auth.currentUser?.email?: "Unknown"
        )

        val dbReference = FirebaseDatabase.getInstance().reference.child("Foods")

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