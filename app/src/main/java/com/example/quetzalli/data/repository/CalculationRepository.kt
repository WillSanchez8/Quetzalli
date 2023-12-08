package com.example.quetzalli.data.repository

import com.example.quetzalli.data.models.Operations
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CalculationRepository @Inject constructor(private val db: FirebaseFirestore){

    //Función para obtener las operaciones
    suspend fun getOperations(): FetchResult<List<Operations>> {
        return try {
            val querySnapshot = db.collection("mathematical_operations").get().await()
            val operations = querySnapshot.toObjects(Operations::class.java)
            FetchResult.Success(operations)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }
}