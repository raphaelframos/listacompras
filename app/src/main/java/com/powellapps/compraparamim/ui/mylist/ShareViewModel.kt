package com.powellapps.compraparamim.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.Share
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.utils.Utils

class ShareViewModel : ViewModel() {

    private var list = MutableLiveData<List<Shopping>>()

    fun getShareShoppings(userId : String) : LiveData<List<Shopping>> {
        var shoppings = ArrayList<Shopping>()
        FirebaseRepository().getSharedIds(userId).addSnapshotListener{ value, e ->
            if(value != null && value.documents.size > 0){
                val shareIds = value.toObjects(Share::class.java)
                shareIds.forEach {
                    val task = FirebaseRepository().getShopping(it.shoppingId).get()
                    while (!task.isSuccessful){
                        Utils().show("Teste")
                    }
                    val shop = task.result!!.toObject(Shopping::class.java)
                    shop?.let { it1 ->
                        if(!shoppings.contains(shop)){
                            shoppings.add(it1)
                        }

                    }
                }
                list.value = shoppings
                Utils().show("Share 2 " + shoppings)
            }

        }
        return list
    }

}