package com.example.translatorapp.repository

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await



class FavoritesRepository{
    private val database: FirebaseFirestore = Firebase.firestore

    suspend fun saveTextToFirestore(text: String,context: Context) {
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid


        if (userId != null) {
            val userDocumentRef = database.collection("users").document(userId)
            val doc = userDocumentRef.get().await()
            val parsedFavorites = (doc as DocumentSnapshot).toObject(FavoriteTranslations::class.java)
            val favoritesToSave = mutableListOf<String>()
            if (parsedFavorites != null) {
                favoritesToSave.addAll(parsedFavorites.FavoritedTranslations)
            }
            favoritesToSave.add(text)
            userDocumentRef.set(mapOf("FavoritedTranslations" to favoritesToSave))
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


    suspend fun getFavorites(): FavoriteTranslations? {

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        val userId = currentUser?.uid


        val data = userId?.let { database.collection("users").document(it).get().await() }

        return (data as DocumentSnapshot).toObject(FavoriteTranslations::class.java)
    }

}
