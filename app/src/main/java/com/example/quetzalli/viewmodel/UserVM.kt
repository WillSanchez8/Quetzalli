package com.example.quetzalli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserVM @Inject constructor(private val userRepo: UserRepository) : ViewModel() {

    // LiveData to observe the user
    private val _user = MutableLiveData<FirebaseUser?>()
    val user: MutableLiveData<FirebaseUser?> get() = _user
    private val _loginError = MutableLiveData<String>()

    // LiveData to observe the result of the user check
    private val _userCheckResult = MutableLiveData<Boolean>()
    val userCheckResult: LiveData<Boolean> get() = _userCheckResult

    // Function to login with Google
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            when (val result = userRepo.loginWithGoogle(idToken)) {
                is FetchResult.Success -> _user.value = result.data
                is FetchResult.Error -> {
                    _loginError.value = "Error al iniciar sesión, por favor regístrese de nuevo."
                }
            }
        }
    }

    // Function to create a new user
    fun createUser(name: String, gender: String, date: String, occupation: String) {
        viewModelScope.launch {
            when (val result = userRepo.createUser(name, gender, date, occupation)) {
                is FetchResult.Success -> _user.value = result.data
                is FetchResult.Error -> {
                    _loginError.value = "Error al crear la cuenta, por favor intente de nuevo."
                }
            }
        }
    }

    // Function to check if the user is logged in
    fun checkIfUserExists(uid: String) {
        viewModelScope.launch {
            when (val result = userRepo.getUserById(uid)) {
                is FetchResult.Success -> {
                    _userCheckResult.value = result.data != null
                }
                is FetchResult.Error -> {
                    _loginError.value = "Error al verificar el usuario, por favor intente de nuevo."
                }
            }
        }
    }

    // Function to get user data
    fun getUserData(uid: String) {
        viewModelScope.launch {
            when (val result = userRepo.getUserById(uid)) {
                is FetchResult.Success -> {
                    _user.value = result.data as FirebaseUser?
                }
                is FetchResult.Error -> {
                    _loginError.value = "Error, por favor intente de nuevo."
                }
            }
        }
    }
}

