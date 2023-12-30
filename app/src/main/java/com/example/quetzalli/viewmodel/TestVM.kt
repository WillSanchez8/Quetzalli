package com.example.quetzalli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.quetzalli.data.models.Test
import com.example.quetzalli.data.models.TestRep
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.data.repository.TestRepository
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TestVM @Inject constructor(private val testRepo: TestRepository) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val tests = MutableLiveData<List<TestRep>?>()

    val lastTestPoint = MutableLiveData<Test?>()

    fun addTestData(collectionName: String, userId: String, scoreTotal: Int, totalTime: String): LiveData<Boolean> {
        val isUploadSuccessful = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val currentDate = Timestamp.now()
            val test = Test(userId, scoreTotal, totalTime, currentDate)
            val result = testRepo.insertTestData(collectionName, test)
            isUploadSuccessful.value = result is FetchResult.Success
            if (result is FetchResult.Error) {
                _error.postValue(result.exception.message ?: "An unknown error occurred")
            }
        }
        return isUploadSuccessful
    }

    fun getTestData() {
        viewModelScope.launch {
            val result = testRepo.getTestData()
            if (result is FetchResult.Success) {
                tests.value = result.data
            } else if (result is FetchResult.Error) {
                _error.postValue(result.exception.message ?: "An unknown error occurred")
            }
        }
    }

    fun getLastTestPoint(userId: String) {
        viewModelScope.launch {
            val result = testRepo.getLastTestPoint(userId)
            if (result is FetchResult.Success) {
                lastTestPoint.value = result.data
            } else if (result is FetchResult.Error) {
                _error.postValue(result.exception.message ?: "An unknown error occurred")
            }
        }
    }


}
