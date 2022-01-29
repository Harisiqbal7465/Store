package com.example.store.presentation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.repository.MainRepository
import com.example.store.repository.data.entities.CustomListData
import com.example.store.utils.Constant.TAG
import com.example.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomListViewModel @Inject constructor(
    private val repository: MainRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(){

    init {
        savedStateHandle.get<String>("customListName")?.let { listName ->
            Log.i(TAG,"ListName = $listName")
            getAllList(listName)
        }
    }

    private val _getCustomList = MutableStateFlow<Resource<List<CustomListData>>>(Resource.Loading())
    val getCustomList: StateFlow<Resource<List<CustomListData>>>
        get() = _getCustomList


    fun getAllList(listName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getALlListOfCustomList(listName).collect {
                Log.i(TAG,"List = ${it.data}")
                _getCustomList.value = it
            }
        }
    }
}