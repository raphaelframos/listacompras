package com.powellapps.buyforme.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Shopping : Serializable {

    var name = 1
    var date = 0L
    var userId = ""
    var userPhoto = ""
    @DocumentId
    var documentId = ""
    var shareId = ""
    var shared = ArrayList<String>()

    constructor(){
        date = Date().time
    }

    fun shareMap(): MutableMap<String, Any> {
        var map = HashMap<String, Any>()
        map.put("shareId", shareId)
        return map
    }


    fun nameFormat(): String {
        return "#" + name
    }

    fun add(userId: String) {
        if(!shared.contains(userId)){
            shared.add(userId)
        }

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Shopping

        if (name != other.name) return false
        if (date != other.date) return false
        if (userId != other.userId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name
        result = 31 * result + date.hashCode()
        result = 31 * result + userId.hashCode()
        return result
    }


}