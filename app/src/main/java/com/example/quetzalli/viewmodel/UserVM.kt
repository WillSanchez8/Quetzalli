package com.example.quetzalli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quetzalli.data.models.User
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(private val userRepo: UserRepository) : ViewModel() {
    private val _signInResult = MutableLiveData<FetchResult<FirebaseUser>>() //Es una variable mutable
    val signInResult: LiveData<FetchResult<FirebaseUser>> get() = _signInResult //Es una variable de solo lectura

    private val _registerResult = MutableLiveData<FetchResult<Void?>>() //Es una variable mutable
    val registerResult: LiveData<FetchResult<Void?>> get() = _registerResult //Es una variable de solo lectura

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = userRepo.signInWithGoogle(idToken)
            _signInResult.value = result
        }
    }

    fun registerUser(user: User) {
        viewModelScope.launch {
            val result = userRepo.registerUser(user)
            _registerResult.value = result
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return userRepo.auth.currentUser
    }

    // Función para obtener un usuario por su id
    fun getUserById(id: String): LiveData<FetchResult<User?>> {
        val result = MutableLiveData<FetchResult<User?>>()
        viewModelScope.launch {
            result.value = userRepo.getUserById(id)
        }
        return result
    }
    // Función para eliminar un usuario
    fun deleteUser(): LiveData<FetchResult<Void?>> {
        val result = MutableLiveData<FetchResult<Void?>>()
        viewModelScope.launch {
            result.value = userRepo.deleteUser()
        }
        return result
    }

    // Función para obtener el nombre del usuario
    fun getUserName(): LiveData<FetchResult<String?>> {
        val result = MutableLiveData<FetchResult<String?>>()
        viewModelScope.launch {
            result.value = userRepo.getUserName()
        }
        return result
    }

    //Función para actualizar el usuario
    fun updateUser(user: User): LiveData<FetchResult<Void?>> {
        val result = MutableLiveData<FetchResult<Void?>>()
        viewModelScope.launch {
            result.value = userRepo.updateUser(user)
        }
        return result
    }

    fun logout() {
        viewModelScope.launch {
            userRepo.logout()
        }
    }
}





