package com.powellapps.compraparamim.ui.newlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.QuerySnapshot
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.ui.mylist.Shopping

class ViewModelNewList : ViewModel() {

    private var products : MutableLiveData<List<Product>> = MutableLiveData()

    fun getProducts(id: String) : LiveData<List<Product>> {
        FirebaseRepository().getProducts(id).addSnapshotListener { value, e ->
            if (value != null) {
                products.value = value.toObjects(Product::class.java)
            }
        }

        return products
    }
}