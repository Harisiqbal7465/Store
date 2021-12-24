package com.example.store.repository

import android.util.Log
import com.example.store.data.entities.MainList
import com.example.store.ui.utils.safeCall
import com.example.store.utils.Constant.TAG
import com.example.store.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainRepository {
    private val mainList = FirebaseFirestore.getInstance().collection("mainList")

    suspend fun mainListCreate(mainListData: MainList )= withContext(Dispatchers.IO){
        safeCall {
            mainList.document(mainListData.listName).set(mainListData).await()
            Resource.Success(mainListData)
        }
    }

    suspend fun allReadyExistAddMainList(listName: String) = withContext(Dispatchers.IO) {
        safeCall {
            val result = mainList.document(listName).get().await().toObject(MainList::class.java) ?: MainList("","")
            Resource.Success(result)
        }
    }
    suspend fun getAllListOfMainList() = withContext(Dispatchers.IO) {
        safeCall {
            val result = mainList.get().await().toObjects(MainList::class.java)
            Resource.Success(result)
            /*var resource: Resource<List<MainList>> = Resource.Loading()
            mainList.addSnapshotListener { value, error ->
                error?.let {
                    resource = Resource.Error(it.message ?: "An unexpected error occured")
                }

                value?.let {
                    val list = it.toObjects(MainList::class.java)
                    Log.i(TAG, "in snapshot $list")
                    resource = Resource.Success(list.toList())
                }
            }

            resource*/
        }
    }

}