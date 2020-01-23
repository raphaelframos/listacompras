package com.powellapps.compraparamim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.Product
import com.powellapps.compraparamim.repository.FirebaseRepository
import java.lang.Exception

class NewListViewModel : ViewModel() {

    private var products : MutableLiveData<List<Product>> = MutableLiveData()

    fun getProducts(shoppingId: String) : LiveData<List<Product>> {

        FirebaseRepository().getProductsBy(shoppingId).addSnapshotListener{ snapshot, e ->
            if(!snapshot!!.isEmpty) {
                try {
                    var sortedList = snapshot.toObjects(Product::class.java).sortedWith(compareBy({ it.name }))
                    val list = sortedList.sortedWith(compareBy({it.purchased}))
                    products.value = list
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
        }

        return products
    }
}