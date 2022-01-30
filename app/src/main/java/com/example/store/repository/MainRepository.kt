package com.example.store.repository

import android.util.Log
import com.example.store.repository.data.entities.MainListData
import com.example.store.repository.data.entities.CustomListData
import com.example.store.utils.Constant.TAG
import com.example.store.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext

class MainRepository {
    private val mainList = FirebaseFirestore.getInstance().collection("mainList")

    fun insertMainList(mainListData: MainListData) = flow<Resource<Any>> {
        try {
            Log.i(TAG, "insert data $mainListData")
            emit(Resource.Loading<Any>())
            mainList.document(mainListData.listName).set(mainListData).await()
            emit(Resource.Success<Any>(mainListData))
        } catch (e: Exception) {
            emit(Resource.Error<Any>(e.message ?: "An unexpected error occured"))
        }
    }

    fun insertCustomList(collectionName: String, customListData: CustomListData) =
        flow<Resource<Any>> {
            try {
                emit(Resource.Loading<Any>())
                mainList.document(collectionName).collection(collectionName)
                    .document(customListData.listName)
                    .set(customListData).await()
                emit(Resource.Success<Any>(Any()))
            } catch (e: Exception) {
                emit(
                    Resource.Error<Any>(
                        e.message ?: "An unexpected error occured"
                    )
                )
            }
        }


    fun allReadyExistAddMainList(listName: String) = flow<Resource<Boolean>> {
        try {
            emit(Resource.Loading<Boolean>())
            val result = mainList.document(listName).get().await().exists()
            emit(Resource.Success<Boolean>(result))
        } catch (e: Exception) {
            emit(
                Resource.Error<Boolean>(
                    e.message ?: "An unexpected error occured"
                )
            )
        }
    }

    fun getALlListOfCustomList(listName: String) = flow<Resource<List<CustomListData>>> {
        try {
            Log.i(TAG, "list name = ${listName}")
            emit(Resource.Loading<List<CustomListData>>())
            val result = mainList.document(listName).collection(listName)
                .get().await()
                .toObjects(CustomListData::class.java).toList()
            Log.i(TAG, "custom list = ${result.onEach { it.listName }}")
            emit(Resource.Success(result))
        } catch (e: Exception) {
            Log.i(TAG, "repository get list error = ${e.message}")
            emit(
                Resource.Error<List<CustomListData>>(
                    e.message ?: "An unexpected error occured"
                )
            )
        }
    }

    /*fun getAllListOfMainList() = flow<Resource<List<MainListData>>> {
        try {
            emit(Resource.Loading<List<MainListData>>())
            val result = mainList.get().await().toObjects(MainListData::class.java).toList()
            emit(Resource.Success<List<MainListData>>(result))
        } catch (e: Exception) {
            emit(
                Resource.Error<List<MainListData>>(
                    e.message ?: "An unexpected error occured"
                )
            )
        }
    }*/
    /*@ExperimentalCoroutinesApi
    fun getALlListOfCustomList(listName: String) = callbackFlow<Resource<List<CustomListData>>> {
        var main: ListenerRegistration? = null
        try {
            trySend(Resource.Loading<List<CustomListData>>())
            //val result = mainList.document(listName).collection(listName).get().await()
            //Resource.Success(result)
           // trySend(Resource.Success<List<CustomListData>>(result))
            main = mainList.document(listName).collection(listName).addSnapshotListener { value, error ->
                error?.let { errorMessage ->
                    trySend(
                        Resource.Error<List<CustomListData>>(
                            errorMessage.message ?: "An unexpected error occured"
                        )
                    )
                    cancel(errorMessage.message.toString())
                }
                value?.let {
                    val list = it.toObjects(CustomListData::class.java)
                    Log.i(TAG, "repository get list = ${list}")
                    trySend(Resource.Success<List<CustomListData>>(list.toList()))
                }
            }

        } catch (e: Exception) {
            Log.i(TAG, "repository get list error = ${e.message}")
            trySend(
                Resource.Error<List<CustomListData>>(
                    e.message ?: "An unexpected error occured"
                )
            )
        } finally {
            awaitClose { main!!.remove() }
        }
    }*/

    @ExperimentalCoroutinesApi
    fun getAllListOfMainList() = callbackFlow<Resource<List<MainListData>>> {
        var main: ListenerRegistration? = null
        try {
            trySend(Resource.Loading<List<MainListData>>())
            main = mainList.addSnapshotListener { value, error ->
                error?.let { errorMessage ->
                    trySend(
                        Resource.Error<List<MainListData>>(
                            errorMessage.message ?: "An unexpected error occured"
                        )
                    )
                    cancel(errorMessage.message.toString())
                }
                value?.let {
                    val list = it.toObjects(MainListData::class.java)
                    Log.i(TAG, "repository get list = $list")
                    trySend(Resource.Success<List<MainListData>>(list.toList()))
                }
            }

        } catch (e: Exception) {
            Log.i(TAG, "repository get list error = ${e.message}")
            trySend(
                Resource.Error<List<MainListData>>(
                    e.message ?: "An unexpected error occured"
                )
            )
        } finally {
            awaitClose { main!!.remove() }
        }
    }

}