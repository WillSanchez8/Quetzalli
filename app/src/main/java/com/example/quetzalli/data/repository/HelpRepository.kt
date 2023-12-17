package com.example.quetzalli.data.repository

import android.util.Log
import com.example.quetzalli.data.models.FAQ
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HelpRepository @Inject constructor(private val db: FirebaseFirestore ) {

    // Función para obtener las preguntas frecuentes
    suspend fun getFAQ(): FetchResult<List<FAQ>> {
        return try {
            val querySnapshot = db.collection("faqs").get().await()
            val faq = querySnapshot.toObjects(FAQ::class.java)
            FetchResult.Success(faq)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }
}