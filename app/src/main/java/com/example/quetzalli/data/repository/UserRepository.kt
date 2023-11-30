package com.example.quetzalli.data.repository

import com.example.quetzalli.data.models.User
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val auth: FirebaseAuth, private val db: FirebaseFirestore){
    suspend fun loginWithGoogle(idToken: String): FetchResult<FirebaseUser> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                FetchResult.Success(user)
            } else {
                FetchResult.Error(Exception("User is null"))
            }
        } catch (e: ApiException) {
            FetchResult.Error(e)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    suspend fun createUser(name: String, gender: String, date: String, occupation: String): FetchResult<FirebaseUser> {
        return try {
            // Obtiene el usuario actual
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // Crea un objeto User con la información del formulario
                val user = User(currentUser.uid, name, currentUser.email!!, gender, date, occupation)

                // Guarda el objeto User en tu base de datos
                user.id?.let { db.collection("users").document(it).set(user).await() }

                FetchResult.Success(currentUser)
            } else {
                FetchResult.Error(Exception("No hay ningún usuario autenticado"))
            }
        } catch (exception: Exception) {
            FetchResult.Error(exception)
        }
    }

    suspend fun getUserById(id: String): FetchResult<User?> {
        return try {
            val userDocument = db.collection("users").document(id).get().await()
            if (userDocument.exists()) {
                val user = userDocument.toObject(User::class.java)
                FetchResult.Success(user)
            } else {
                FetchResult.Success(null)
            }
        } catch (exception: Exception) {
            FetchResult.Error(exception)
        }
    }




    suspend fun logout(googleSignInClient: GoogleSignInClient) {
        withContext(Dispatchers.IO) {
            googleSignInClient.signOut().await()
        }
    }

}
