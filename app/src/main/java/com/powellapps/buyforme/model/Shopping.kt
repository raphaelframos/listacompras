package com.powellapps.buyforme.model

import com.google.firebase.firestore.DocumentId
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Shopping : Serializable {

    var name = ""
    var date = 0L
    var userId = ""
    var userPhoto = ""
    @DocumentId
    var documentId = ""
    var shared = ArrayList<User>()

    constructor(){
        date = Date().time
    }

    fun add(user: User) {
        if(!shared.contains(user)){
            shared.add(user)
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




}