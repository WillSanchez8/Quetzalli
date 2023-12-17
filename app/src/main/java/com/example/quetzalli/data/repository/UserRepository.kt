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
                user?.id = documentSnapshot.id
                FetchResult.Success(user)
            } else {
                // El usuario no existe, devuelve null
                FetchResult.Success(null)
            }
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    //Funcion para borrar un usuario
    suspend fun deleteUser(): FetchResult<Void?> {
        return try {
            auth.currentUser?.delete()?.await()
            FetchResult.Success(null)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    //Función para obtener el nombre del usuario
    suspend fun getUserName(): FetchResult<String?> {
        return try {
            val documentSnapshot = db.collection("users").document(auth.currentUser!!.uid).get().await()
            if (documentSnapshot.exists()) {
                // El usuario existe, devuelve el nombre del usuario
                val user = documentSnapshot.toObject(User::class.java)
                user?.id = documentSnapshot.id
                FetchResult.Success(user?.name)
            } else {
                // El usuario no existe, devuelve null
                FetchResult.Success("user")
            }
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    //Función para cerrar sesión
    fun logout() {
        auth.signOut()
    }

    //Función para actualizar el usuario
    suspend fun updateUser(id: String, name: String, date: String, occupation: String): FetchResult<Void?> {
        return try {
            val documentReference = db.collection("users").document(id)
            val userUpdates = hashMapOf<String, Any>(
                "name" to name,
                "date" to date,
                "occupation" to occupation
            )
            documentReference.update(userUpdates).await()
            FetchResult.Success(null)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }
}
