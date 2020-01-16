package com.powellapps.compraparamim.ui.mylist

import com.google.firebase.firestore.DocumentId
import java.io.Serializable

class Shopping : Serializable {

    var name = ""
    var date : Long = 0
    var adminUser = ""
    @DocumentId
    var documentId = ""
    var shareId = ""
    var sharePassword = ""

    fun map() : HashMap<String, Any>{
        val map = HashMap<String, Any>()
        map.put("name", name)
        map.put("date", date)
        map.put("adminUser", adminUser)
        return map
    }

    fun shareMap(): MutableMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("shareId", shareId)
        map.put("sharePassword", sharePassword)
        return map
    }


}