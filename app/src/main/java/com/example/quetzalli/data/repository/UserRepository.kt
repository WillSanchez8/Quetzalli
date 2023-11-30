package com.example.quetzalli.data.repository

import com.example.quetzalli.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(val auth: FirebaseAuth, private val db: FirebaseFirestore){

    //Función para iniciar sesión con Google
    suspend fun signInWithGoogle(idToken: String): FetchResult<FirebaseUser> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        return try {
            val result = auth.signInWithCredential(credential).await()
            FetchResult.Success(result.user!!)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    //Función para registrar un usuario
    suspend fun registerUser(user: User): FetchResult<Void?> {
        return try {
            val documentReference = db.collection("users").document(user.id!!)
            documentReference.set(user).await()
            FetchResult.Success(null)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    // Función para obtener un usuario por su id
    suspend fun getUserById(id: String): FetchResult<User?> {
        return try {
            val documentSnapshot = db.collection("users").document(id).get().await()
            if (documentSnapshot.exists()) {
                // El usuario existe, devuelve el usuario
                val user = documentSnapshot.toObject(User::class.java)
                FetchResult.Success(user)
            } else {
                // El usuario no existe, devuelve null
                FetchResult.Success(null)
            }
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }
}
