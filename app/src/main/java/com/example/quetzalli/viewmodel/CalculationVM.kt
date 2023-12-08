package com.example.quetzalli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.quetzalli.data.models.Operations
import com.example.quetzalli.data.repository.CalculationRepository
import com.example.quetzalli.data.repository.FetchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculationVM @Inject constructor(private val calculRep : CalculationRepository) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    val operations: LiveData<List<Operations>> = liveData {
        val data = calculRep.getOperations()
        if (data is FetchResult.Success) {
            emit(data.data)
        } else if (data is FetchResult.Error) {
            _error.postValue(data.exception.message ?: "An unknown error occurred")
            emit(emptyList())
        }
    }
}