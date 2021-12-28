package com.example.store.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.repository.data.entities.MainList
import com.example.store.repository.MainRepository
import com.example.store.utils.Constant.TAG
import com.example.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _checkExistingMainListStatus =
        MutableStateFlow<Resource<MainList>>(Resource.Loading())
    val checkExistingMainListStatus: StateFlow<Resource<MainList>>
        get() = _checkExistingMainListStatus

    private val _listOfMainListStatus =
        MutableStateFlow<Resource<List<MainList>>>(Resource.Loading())
    val listOfMainListStatus: StateFlow<Resource<List<MainList>>>
        get() = _listOfMainListStatus

    fun getAllListOfMainList() {
        viewModelScope.launch {
           val result = repository.getAllListOfMainList()
            Log.i(TAG,"in get all list ${result.data}")
            _listOfMainListStatus.value = result
        }
    }

    fun checkExistingMainList(mainListName: String) {
        viewModelScope.launch {
            _checkExistingMainListStatus.value = repository.allReadyExistAddMainList(mainListName)
        }
    }

    fun addMainList(mainList: MainList) {
        viewModelScope.launch {
            repository.mainListCreate(mainList)
        }
    }
}