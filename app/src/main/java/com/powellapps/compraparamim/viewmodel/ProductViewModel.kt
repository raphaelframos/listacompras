package com.powellapps.compraparamim.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.repository.FirebaseRepository

class ProductViewModel : ViewModel() {

    private val products = MutableLiveData<List<String>>()

    fun getProducts(id: String): LiveData<List<String>> {
        var result = HashMap<String, Int>()
        FirebaseRepository().getMostProducts(id).addSnapshotListener{ value, e->
            value!!.forEach {
                val name : String = it.get("name") as String
                if(result.contains(name)){
                    result[name] = result.getValue(name) + 1
                }else{
                    result.put(name, 1)
                }
            }
            val names = result.toList().sortedBy { (_, value) -> value}.toMap()
            products.value = names.keys.toList().reversed()
        }
        return products
    }
}