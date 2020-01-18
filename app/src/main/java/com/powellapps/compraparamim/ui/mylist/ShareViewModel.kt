package com.powellapps.compraparamim.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
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
                var tasks = ArrayList<Task<DocumentSnapshot>>()
                shareIds.forEach {
                    val task = FirebaseRepository().getShopping(it.shoppingId).get()
                    tasks.add(task)
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnCompleteListener {
                    it.result?.forEach {
                        val shop = it.toObject(Shopping::class.java)
                        shop?.let { it1 ->
                            if(!shoppings.contains(shop)){
                                shoppings.add(it1)
                            }
                        }
                    }
                    list.value = shoppings

                }

            }

        }
        return list
    }

}