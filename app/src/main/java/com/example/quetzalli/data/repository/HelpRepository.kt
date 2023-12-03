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

            // Agrega un log para registrar la información recuperada
            Log.d("HelpRepository", "Se recuperaron las siguientes preguntas frecuentes: $faq")

            FetchResult.Success(faq)
        } catch (e: Exception) {
            // Agrega un log para registrar el error
            Log.e("HelpRepository", "Error al obtener las preguntas frecuentes", e)

            FetchResult.Error(e)
        }
    }
}