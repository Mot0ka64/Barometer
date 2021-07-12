package com.motoka64.barometer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.motoka64.barometer.database.BarometricData
import com.motoka64.barometer.database.BarometricRepository
import kotlinx.coroutines.launch

class BarometerViewModel(private val repository: BarometricRepository) : ViewModel() {
    val allData = repository.allData.asLiveData()

    fun insert(data: BarometricData) = viewModelScope.launch {
        repository.insert(data)
    }
}

class BarometerViewModelFactory(private val repository: BarometricRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarometerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarometerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class!")
    }
}