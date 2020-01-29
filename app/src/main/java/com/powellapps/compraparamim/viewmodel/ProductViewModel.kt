package com.powellapps.compraparamim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.MostUsedProduct
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.model.Product
import com.powellapps.compraparamim.model.ReferenceProduct
import com.powellapps.compraparamim.utils.Utils
import java.lang.Exception

class ProductViewModel : ViewModel() {

    private val products = MutableLiveData<List<MostUsedProduct>>()
    private val referenceProducts = MutableLiveData<List<ReferenceProduct>>()

    fun getProducts(id: String): LiveData<List<MostUsedProduct>> {

        FirebaseRepository().getMostProducts(id).addSnapshotListener{ value, e->
            try {
                value?.let {
                    val list = it!!.toObjects(Product::class.java)
                    var mostUsed = ArrayList<MostUsedProduct>()
                    list.groupBy { it.name }.entries.map { (name, group) ->
                        var product = MostUsedProduct(name, group)
                        mostUsed.add(product)
                    }
                    products.value = mostUsed.sortedByDescending { it.list.size }
                }

            }catch (e : Exception){
                e.printStackTrace()
            }
        }
        return products
    }

    fun getReferenceProducts(userId: String): LiveData<List<ReferenceProduct>> {
        FirebaseRepository().getReferenceProducts(userId).addSnapshotListener{ value, e->

            value?.let {
                val list = it.toObjects(ReferenceProduct::class.java)
                referenceProducts.value = list
            }

        }
        return referenceProducts
    }


}