package com.example.store.presentation.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.repository.MainRepository
import com.example.store.repository.data.entities.MainListData
import com.example.store.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainListDetailViewModel @Inject constructor(
    private val repository: MainRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    init {
        savedStateHandle.get<MainListData>("mainListId")?.let { listId ->
            mainListInfo(listId.documentId)
        }
    }

    private val _getMainListInfo =
        MutableStateFlow<Resource<MainListData>>(Resource.Loading())
    val getMainListInfo: StateFlow<Resource<MainListData>>
        get() = _getMainListInfo

    private fun mainListInfo(mainListId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMainListInfo(mainListId).collect {
                _getMainListInfo.value = it
            }
        }
    }
}