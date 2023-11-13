package com.example.quetzalli.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val auth: FirebaseAuth, db : FirebaseFirestore){
    suspend fun handleSignInResult(completedTask: Task<GoogleSignInAccount>): FetchResult<FirebaseUser> {
        return try {
            val account = completedTask.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                FetchResult.Success(user)
            } else {
                FetchResult.Error(Exception("User is null"))
            }
        } catch (e: ApiException) {
            // Maneja específicamente las ApiException
            FetchResult.Error(e)
        } catch (e: Exception) {
            // Maneja todas las demás excepciones
            FetchResult.Error(e)
        }
    }

    suspend fun logout(googleSignInClient: GoogleSignInClient) {
        withContext(Dispatchers.IO) {
            googleSignInClient.signOut().await()
        }
    }

    suspend fun addUser(){
        // Implementa este método según tus necesidades
    }

}
