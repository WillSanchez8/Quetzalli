package com.example.quetzalli.viewmodel

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

    // Function to login with Google
    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            when (val result = userRepo.loginWithGoogle(idToken)) {
                is FetchResult.Success -> _user.value = result.data
                is FetchResult.Error -> {
                    // Handle error
                }
            }
        }
    }
}

