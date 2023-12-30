package com.example.quetzalli.data.repository

import com.example.quetzalli.data.models.Test
import com.example.quetzalli.data.models.TestRep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class TestRepository @Inject constructor(private val db: FirebaseFirestore) {

    // Función para insertar los datos de la prueba
    suspend fun insertTestData(collectionName: String, test: Test): FetchResult<DocumentReference> {
        return try {
            test.completed = true // Establece isCompleted en true cuando la prueba se completa
            test.type = collectionName // Establece el tipo de prueba
            val documentReference = db.collection(collectionName).add(test).await()
            FetchResult.Success(documentReference)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    // Función para obtener los datos de la prueba
    suspend fun getTestData(): FetchResult<List<TestRep>> {
        return try {
            val testList = mutableListOf<TestRep>()
            val querySnapshot = db.collection("testdata").get().await()
            for (document in querySnapshot.documents) {
                val test = document.toObject(TestRep::class.java)
                if (test != null) {
                    testList.add(test)
                }
            }
            FetchResult.Success(testList)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    //Función para obtener la ultima fecha de la prueba
    suspend fun getLastTestPoint(userId: String): FetchResult<Test?> {
        return try {
            val querySnapshot = db.collection("testcalculation")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
            val test = querySnapshot.documents[0].toObject(Test::class.java)
            FetchResult.Success(test)
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

    // Función para obtener los datos de la prueba
    suspend fun getNextTest(): FetchResult<Test> {
        return try {
            val collections = listOf("testdata", "testcalculation", "testmemory")
            for (collection in collections) {
                val querySnapshot = db.collection(collection).whereEqualTo("completed", false).limit(1).get().await()
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val test = document.toObject(Test::class.java)
                    if (test != null) {
                        return FetchResult.Success(test)
                    }
                }
            }
            FetchResult.Error(Exception("No hay más pruebas disponibles"))
        } catch (e: Exception) {
            FetchResult.Error(e)
        }
    }

}
