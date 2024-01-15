package com.example.translatorapp.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class FavoiritesRepository{
    private val database: FirebaseFirestore = Firebase.firestore

    fun saveTextToFirestore(text: String,context: Context) {
        val firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid


        if (userId != null) {
            val userDocumentRef = database.collection("users").document(userId)
            userDocumentRef.set(hashMapOf<String, Any>())
            userDocumentRef.update(
                hashMapOf("FavoritedTranslations" to text) as Map<String, Any>)

                .addOnSuccessListener {
                    // Text saved successfully
                    Toast.makeText(context, "Translation favorited", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Error occurred while saving the text
                    Toast.makeText(context, "Error favoriting translation", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun getTextFromLoggedInUser()
    {
        val firebaseAuth: FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid


        if (userId != null) {
            val userDocumentRef = database.collection("users").document(userId)

            userDocumentRef.get()
                .addOnSuccessListener {

                }
                .addOnFailureListener {

                }
        }

    }

}
