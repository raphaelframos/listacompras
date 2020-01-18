package com.powellapps.compraparamim.ui.newlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.repository.FirebaseRepository

class ViewModelNewList : ViewModel() {

    private var products : MutableLiveData<List<Product>> = MutableLiveData()

    fun getProducts(shoppingId: String) : LiveData<List<Product>> {

        FirebaseRepository().getShopping(shoppingId).addSnapshotListener{ snapshot, e ->
            if(snapshot?.exists()!!) {
                var sortedList = snapshot?.toObject(Shopping::class.java)!!.products.sortedWith(compareBy({ it.name }))
                products.value = sortedList
            }
        }

        return products
    }
}