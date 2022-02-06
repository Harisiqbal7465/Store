package com.example.store.repository

import android.util.Log
import com.example.store.repository.data.entities.CustomListData
import com.example.store.repository.data.entities.MainListData
import com.example.store.utils.Constant.TAG
import com.example.store.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await


class MainRepository {
    private val mainList = FirebaseFirestore.getInstance().collection("mainList")

    fun insertMainList(mainListData: MainListData) = flow<Resource<Any>> {
        try {
            Log.i(TAG, "insert data $mainListData")
            emit(Resource.Loading())
            mainList.document(mainListData.listName).set(mainListData).await()
            emit(Resource.Success(mainListData))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    fun insertCustomList(collectionName: String, customListData: CustomListData) =
        flow{
            try {
                emit(Resource.Loading())
                mainList.document(collectionName).collection(collectionName)
                    .document(customListData.listName)
                    .set(customListData).await()
                emit(Resource.Success(Any()))
            } catch (e: Exception) {
                emit(
                    Resource.Error(
                        e.message ?: "An unexpected error occurred"
                    )
                )
            }
        }


    fun allReadyExistAddMainList(listName: String) = flow {
        try {
            emit(Resource.Loading())
            val result = mainList.whereEqualTo("listName", listName).get().await()
                .toObjects(MainListData::class.java).toList()
            Log.i(TAG,"already exist ${result.isNotEmpty()}")
            emit(Resource.Success(result.isNotEmpty()))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.message ?: "An unexpected error occurred"
                )
            )
        }
    }

    fun getMainListInfo(listId: String) = flow<Resource<MainListData>> {
        try {
            emit(Resource.Loading())
            val result = mainList.document(listId).get().await().toObject(MainListData::class.java)!!
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(
                Resource.Error(
                    e.message ?: "An unexpected error occurred"
                )
            )
        }
    }

    @ExperimentalCoroutinesApi
    fun getALlListOfCustomList(listName: String) = callbackFlow<Resource<List<CustomListData>>> {
        var main: ListenerRegistration? = null
        try {
            trySend(Resource.Loading())
            main = mainList.document(listName).collection(listName)
                .addSnapshotListener { value, error ->
                    error?.let { errorMessage ->
                        trySend(
                            Resource.Error(
                                errorMessage.message ?: "An unexpected error occurred"
                            )
                        )
                        cancel(errorMessage.message.toString())
                    }
                    value?.let {
                        val list = it.toObjects(CustomListData::class.java)
                        trySend(Resource.Success(list.toList()))
                    }
                }

        } catch (e: Exception) {
            Log.i(TAG, "repository get list error = ${e.message}")
            trySend(
                Resource.Error(
                    e.message ?: "An unexpected error occurred"
                )
            )
        } finally {
            awaitClose { main!!.remove() }
        }
    }

    @ExperimentalCoroutinesApi
    fun getAllListOfMainList() = callbackFlow<Resource<List<MainListData>>> {
        var main: ListenerRegistration? = null
        try {
            trySend(Resource.Loading())
            main = mainList.addSnapshotListener { value, error ->
                error?.let { errorMessage ->
                    trySend(
                        Resource.Error(
                            errorMessage.message ?: "An unexpected error occurred"
                        )
                    )
                    cancel(errorMessage.message.toString())
                }
                value?.let { querySnapshot ->
                    val list = querySnapshot
                        .toObjects(MainListData::class.java)
                        .toList()
                        .mapIndexed { index, mainListData ->
                            mainListData.documentId = querySnapshot.documents[index].id
                            mainListData
                        }
                    Log.i(TAG,"list in repository ${list}")
                    trySend(
                        Resource.Success(
                            list
                        )
                    )
                }
            }

        } catch (e: Exception) {
            Log.i(TAG, "repository get list error = ${e.message}")
            trySend(
                Resource.Error(
                    e.message ?: "An unexpected error occurred"
                )
            )
        } finally {
            awaitClose { main!!.remove() }
        }
    }

}
