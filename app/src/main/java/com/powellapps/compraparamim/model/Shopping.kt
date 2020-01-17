package com.powellapps.compraparamim.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import com.powellapps.compraparamim.ui.newlist.Product
import com.powellapps.compraparamim.viewmodel.ProductViewModel
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Shopping : Serializable {

    var name = 1
    var date = 0L
    var userId = ""
    @DocumentId
    var documentId = ""
    var shareId = ""
    var sharePassword = ""
    var products = ArrayList<Product>()
    var shared = ArrayList<String>()

    constructor(){
        date = Date().time
    }

    fun map() : HashMap<String, Any>{
        val map = HashMap<String, Any>()
        map.put("name", name)
        map.put("date", date)
        map.put("userId", userId)
        map.put("products", products)
        map.put("shared", shared)
        return map
    }

    fun shareMap(): MutableMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("shareId", shareId)
        map.put("sharePassword", sharePassword)
        return map
    }


    fun documentIdMap(): MutableMap<String, String> {
        var map = HashMap<String, String>()
        map.put("documentId", documentId)
        return map
    }

    fun add(product: Product){
        products.add(product)
    }

    fun nameFormat(): String {
        return "#" + name
    }

    fun add(userId: String) {
        if(!shared.contains(userId)){
            shared.add(userId)
        }

    }


}