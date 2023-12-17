package com.example.quetzalli.data.repository

import com.example.quetzalli.data.models.Test
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    suspend fun hasUserCompletedAllTests(userId: String): FetchResult<Boolean> {
        return try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val collections = listOf("testcalculation", "testmemory", "testspace")
            var totalTests = 0
            for (collection in collections) {
                val querySnapshot = db.collection(collection)
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("date", currentDate)
                    .get()
                    .await()
                totalTests += querySnapshot.size()
            }
            FetchResult.Success(totalTests >= 3)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

}
