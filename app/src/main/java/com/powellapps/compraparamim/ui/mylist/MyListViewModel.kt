package com.powellapps.compraparamim.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.utils.Utils

class MyListViewModel : ViewModel() {

    private var list = MutableLiveData<List<Shopping>>()

    fun getList(id: String) : LiveData<List<Shopping>> {
        FirebaseRepository().getLists(id).addSnapshotListener {value, _ ->
            list.value = value?.toObjects(Shopping::class.java)
        }

        return list
    }
}