package com.example.store.presentation.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.store.repository.MainRepository
import com.example.store.repository.data.entities.CustomListData
import com.example.store.utils.Constant.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomListAddViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    fun insertCustomList(listName: String, customList: CustomListData) {
        viewModelScope.launch {
            Log.i(TAG,"In view model")
            repository.insertCustomList(listName, customList).collect()
        }
    }
}