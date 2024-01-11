package com.example.quetzalli.data.repository

import android.util.Log
import com.example.quetzalli.data.models.DataTraining
import com.example.quetzalli.data.models.User
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.firestore.Query

class UserRepository @Inject constructor(
    val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val googleSignInClient: GoogleSignInClient
) {

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
            val documentSnapshot =
                db.collection("users").document(auth.currentUser!!.uid).get().await()
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

        googleSignInClient.signOut()
    }

    //Función para actualizar el usuario
    suspend fun updateUser(
        id: String,
        name: String,
        date: String,
        occupation: String
    ): FetchResult<Void?> {
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

    //Función para obtener información de pruebas y del usuario
    suspend fun getDataFromCollections(userId: String): FetchResult<DataTraining> {
        val collections = listOf("testmemory", "testcalculation", "testspace")
        var data: DataTraining? = null
        try {
            val userDocument = db.collection("users").document(userId).get().await()
            val antecedents = userDocument.get("antecedents") as Long
            val gender = userDocument.get("gender") as Long

            for (collection in collections) {
                val document = db.collection(collection)
                    .orderBy("date", Query.Direction.DESCENDING)
                    .limit(1)
                    .get()
                    .await()
                    .documents
                    .firstOrNull()

                if (document != null) {
                    val score = document.get("scoreTotal") as Long
                    val time = convertTimeToDecimal(document.get("totalTime") as String)

                    data = when (collection) {
                        "testmemory" -> DataTraining(antecedents, gender, 0, 0f, score, time, 0, 0f)
                        "testcalculation" -> DataTraining(antecedents, gender, 0, 0f, 0, 0f, score, time)
                        "testspace" -> DataTraining(antecedents, gender, score, time, 0, 0f, 0, 0f)
                        else -> throw IllegalArgumentException("Invalid collection name")
                    }
                }
            }
            if (data != null) {
                return FetchResult.Success(data)
            } else {
                throw Exception("No data found")
            }
        } catch (e: Exception) {
            return FetchResult.Error(e)
        }
    }
    /*
    suspend fun getDataFromCollections(userId: String): FetchResult<List<DataTraining>> {
        val collections = listOf("testmemory", "testcalculation", "testspace")
        val dataList = mutableListOf<DataTraining>()
        try {
            val userDocument = db.collection("users").document(userId).get().await()
            val antecedents = userDocument.get("antecedents") as Long
            val gender = userDocument.get("gender") as Long

            for (collection in collections) {
                val documents = db.collection(collection).get().await()
                for (document in documents) {
                    val score = document.get("scoreTotal") as Long
                    val time = convertTimeToDecimal(document.get("totalTime") as String)

                    val data = when (collection) {
                        "testmemory" -> DataTraining(antecedents, gender, 0, 0f, score, time, 0, 0f)
                        "testcalculation" -> DataTraining(antecedents, gender, 0, 0f, 0, 0f, score, time)
                        "testspace" -> DataTraining(antecedents, gender, score, time, 0, 0f, 0, 0f)
                        else -> throw IllegalArgumentException("Invalid collection name")
                    }
                    dataList.add(data)
                }
            }
            Log.d("DataTraining", dataList.toString())
            return FetchResult.Success(dataList)
        } catch (e: Exception) {
            Log.d("DataTraining", e.toString())
            return FetchResult.Error(e)
        }
    }
    */

    private fun convertTimeToDecimal(time: String): Float {
        val parts = time.split(":")
        val minutes = parts[0]
        val seconds = parts[1]
        return "$minutes.$seconds".toFloat()
    }

}
