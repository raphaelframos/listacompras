package com.powellapps.compraparamim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.MostUsedProduct
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.model.Product
import java.lang.Exception

class ProductViewModel : ViewModel() {

    private val products = MutableLiveData<List<MostUsedProduct>>()

    fun getProducts(id: String): LiveData<List<MostUsedProduct>> {

        FirebaseRepository().getMostProducts(id).addSnapshotListener{ value, e->
            try {
                val list = value!!.toObjects(Product::class.java)
                var mostUsed = ArrayList<MostUsedProduct>()
                list.groupBy { it.name }.entries.map { (name, group) ->
                    var product = MostUsedProduct(name, group)
                    mostUsed.add(product)
                }
                products.value = mostUsed.sortedByDescending { it.list.size }
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        return products
    }


}