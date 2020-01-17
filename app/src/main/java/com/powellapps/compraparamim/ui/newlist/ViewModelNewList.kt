package com.powellapps.compraparamim.ui.newlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.utils.Utils

class ViewModelNewList : ViewModel() {

    private var products : MutableLiveData<List<Product>> = MutableLiveData()

    fun getProducts(shoppingId: String) : LiveData<List<Product>> {

        FirebaseRepository().getProducts(shoppingId).addSnapshotListener{snapshot, e ->
            if(snapshot?.exists()!!) {
                products.value = snapshot?.toObject(Shopping::class.java)!!.products
            }
        }

        return products
    }
}