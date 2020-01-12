package com.powellapps.compraparamim.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyListViewModel : ViewModel() {

    private val shoppingList = MutableLiveData<List<Shopping>>().apply {

        var list = ArrayList<Shopping>()
        list.add(Shopping())
        list.add(Shopping())
        list.add(Shopping())
        list.add(Shopping())
        value = list
    }
    val list: LiveData<List<Shopping>> = shoppingList
}