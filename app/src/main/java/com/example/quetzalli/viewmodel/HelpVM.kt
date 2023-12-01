package com.example.quetzalli.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quetzalli.data.models.FAQ
import com.example.quetzalli.data.repository.FetchResult
import com.example.quetzalli.data.repository.HelpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpVM @Inject constructor(private val helpRepo : HelpRepository) : ViewModel() {

    val faqList = MutableLiveData<List<FAQ?>?>()

    fun getFAQs(){
        faqList.value = List(10) {null} // Muestra el efecto de carga
        viewModelScope.launch{
            when(val result = helpRepo.getFAQ()){
                is FetchResult.Success -> {
                    faqList.postValue(result.data)
                }
                is FetchResult.Error -> {
                    faqList.postValue(emptyList())
                }
            }
        }
    }
}
