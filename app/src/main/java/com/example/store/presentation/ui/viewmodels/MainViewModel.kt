package com.example.store.presentation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.repository.data.entities.MainListData
import com.example.store.repository.MainRepository
import com.example.store.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    init {
        getAllListOfMainList()
    }

    private val _checkExistingMainListStatus =
        MutableStateFlow<Resource<Boolean>>(Resource.Loading())
    val checkExistingMainListDataStatus: StateFlow<Resource<Boolean>>
        get() = _checkExistingMainListStatus

    private val _listOfMainListStatus =
        MutableStateFlow<Resource<List<MainListData>>>(Resource.Loading())
    val listOfMainListDataStatus: StateFlow<Resource<List<MainListData>>>
        get() = _listOfMainListStatus

    @ExperimentalCoroutinesApi
    fun getAllListOfMainList() {
        viewModelScope.launch(Dispatchers.IO) {
            val mainList = FirebaseFirestore.getInstance().collection("mainList")
            val result = mainList.get().await().toObjects(MainListData::class.java).toList()
            repository.getAllListOfMainList().collect {
                _listOfMainListStatus.value = it
            }
        }
    }

    fun checkExistingMainList(mainListName: String) {
        viewModelScope.launch {
            repository.allReadyExistAddMainList(mainListName).collect {
                _checkExistingMainListStatus.value = it
            }
        }
    }

    fun addMainList(mainListData: MainListData) {
        viewModelScope.launch {
            repository.insertMainList(mainListData).collect()
        }
    }
}