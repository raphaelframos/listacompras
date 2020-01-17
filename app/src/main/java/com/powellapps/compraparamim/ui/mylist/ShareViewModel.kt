package com.powellapps.compraparamim.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.compraparamim.model.Share
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.utils.Utils

class ShareViewModel : ViewModel() {

    private var list = MutableLiveData<List<Share>>()

    fun getShareIds(userId : String) : LiveData<List<Share>> {
        FirebaseRepository().getSharedIds(userId).addSnapshotListener{ value, e ->
            if(value != null && value.documents.size > 0){
                list.value = value.toObjects(Share::class.java)
            }
            Utils().show("Share " + value!!.toObjects(Share::class.java))
        }
        return list
    }

}