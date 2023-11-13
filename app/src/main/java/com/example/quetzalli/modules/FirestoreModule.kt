package com.example.quetzalli.modules

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {
    //provide firebase instance
    @Singleton //permite que solo exista una instancia de la clase
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
}