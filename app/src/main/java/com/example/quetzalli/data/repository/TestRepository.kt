package com.example.quetzalli.data.repository

import com.example.quetzalli.data.models.Test
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TestRepository @Inject constructor(private val db: FirebaseFirestore) {

    // Función para insertar los datos de la prueba
    suspend fun insertTestData(collectionName: String, test: Test): FetchResult<DocumentReference> {
        return try {
            val documentReference = db.collection(collectionName).add(test).await()
            FetchResult.Success(documentReference)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }
}
