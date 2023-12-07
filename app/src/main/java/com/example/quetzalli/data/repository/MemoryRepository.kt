package com.example.quetzalli.data.repository

import com.example.quetzalli.data.models.SequenceGraph
import com.example.quetzalli.data.models.TestMemory
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MemoryRepository @Inject constructor(private val db: FirebaseFirestore){

    // Función para obtener las secuencias gráficas
    suspend fun getSequences(): FetchResult<List<SequenceGraph>> {
        return try {
            val querySnapshot = db.collection("sequences").get().await()
            val sequences = querySnapshot.toObjects(SequenceGraph::class.java)
            FetchResult.Success(sequences)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    // Función para insertar los datos de la prueba de memoria
    suspend fun insertTestMemoryData(testMemory: TestMemory): FetchResult<DocumentReference> {
        return try {
            val documentReference = db.collection("testmemory").add(testMemory).await()
            FetchResult.Success(documentReference)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

}

