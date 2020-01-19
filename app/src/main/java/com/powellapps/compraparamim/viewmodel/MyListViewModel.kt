package com.powellapps.compraparamim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.repository.FirebaseRepository

class MyListViewModel : ViewModel() {

    private var list = MutableLiveData<List<Shopping>>()

    fun getList(id: String) : LiveData<List<Shopping>> {
        FirebaseRepository().getListsById(id).addSnapshotListener { snapshot, e ->

            if (snapshot != null && !snapshot.isEmpty) {
                val shoppings = snapshot.toObjects(Shopping::class.java)
                var sortedList = shoppings.sortedWith(compareBy({ it.date }))
                list.value = sortedList.reversed()
            }
        }

        return list
    }
}