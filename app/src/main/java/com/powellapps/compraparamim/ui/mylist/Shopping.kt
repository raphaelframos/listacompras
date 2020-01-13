package com.powellapps.compraparamim.ui.mylist

import com.google.firebase.firestore.DocumentId

class Shopping {

    var name = ""
    var date : Long = 0
    var adminUser = ""
    @DocumentId
    var id = ""

    fun map() : HashMap<String, Any>{
        val map = HashMap<String, Any>()
        map.put("name", name)
        map.put("date", date)
        map.put("adminUser", adminUser)
        return map
    }
}