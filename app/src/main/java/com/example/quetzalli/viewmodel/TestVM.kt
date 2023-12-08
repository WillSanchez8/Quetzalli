package com.example.quetzalli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quetzalli.data.models.Test
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.data.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestVM @Inject constructor(private val testRepo: TestRepository) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun addTestData(collectionName: String, userId: String, scoreTotal: Int, totalTime: String): LiveData<Boolean> {
        val isUploadSuccessful = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val test = Test(userId, scoreTotal, totalTime)
            val result = testRepo.insertTestData(collectionName, test)
            isUploadSuccessful.value = result is FetchResult.Success
            if (result is FetchResult.Error) {
                _error.postValue(result.exception.message ?: "An unknown error occurred")
            }
        }
        return isUploadSuccessful
    }

}
