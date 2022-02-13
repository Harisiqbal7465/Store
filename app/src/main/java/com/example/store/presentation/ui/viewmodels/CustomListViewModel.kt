package com.example.store.presentation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.presentation.ui.utils.EditingState
import com.example.store.repository.MainRepository
import com.example.store.repository.data.entities.CustomListData
import com.example.store.repository.data.entities.MainListData
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

@ExperimentalCoroutinesApi
@HiltViewModel
class CustomListViewModel @Inject constructor(
    private val repository: MainRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        savedStateHandle.get<MainListData>("mainListData")?.let {
            getAllList(it.documentId)
        }
    }

    private var _editState: EditingState = EditingState.EXISTING_MAIN_LIST
    val editState get() = _editState

    private var _priceList: MutableList<Int> = mutableListOf()
    val priceList: List<Int> get() = _priceList

    private var _weightList: MutableList<Int> = mutableListOf()
    val weightList: List<Int> get() = _weightList

    private var _currentListId :String = ""
    val currentListId get() = _currentListId

    private var _currentCustomListItem: CustomListData? = null
    val currentCustomListItem get() = _currentCustomListItem

    fun setCurrentCustomListItem(listItem: CustomListData) {
        _currentCustomListItem = listItem
    }

    fun setCurrentListId(id: String){
        _currentListId = id
    }
    fun setPriceList(itemPrice: Int) {
        _priceList.add(itemPrice)
    }

    fun setWeightList(itemWeight: Int) {
        _weightList.add(itemWeight)
    }

    fun setEditState(state: EditingState) {
        _editState = state
    }

    fun insertCustomList(listName: String, customList: CustomListData) {
        viewModelScope.launch {
            repository.insertCustomList(listName, customList).collect()
        }
    }

    private val _getCustomList =
        MutableStateFlow<Resource<List<CustomListData>>>(Resource.Loading())
    val getCustomList: StateFlow<Resource<List<CustomListData>>>
        get() = _getCustomList

    private fun getAllList(listName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getALlListOfCustomList(listName).collect {
                _getCustomList.value = it
            }
        }
    }

    fun updateCustomList(mainListId: String, customListId: String, customListData: CustomListData) {
        viewModelScope.launch {
            repository.updateCustomList(mainListId, customListId, customListData)
        }
    }

    fun deleteCustomList(mainListId: String, customListId: String) {
        viewModelScope.launch {
            repository.deleteCustomList(mainListId, customListId).collect()
        }
    }

    fun searchCustomList(mainListId: String, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.searchCustomList(mainListId, text).collect {
                _getCustomList.value = it
            }
        }
    }
}