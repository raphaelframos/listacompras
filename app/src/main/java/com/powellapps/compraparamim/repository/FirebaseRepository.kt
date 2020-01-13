package com.powellapps.compraparamim.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.powellapps.compraparamim.ui.mylist.Shopping
import com.powellapps.compraparamim.ui.newlist.Product


class FirebaseRepository {

    private val LISTS = "lists"

    private fun getDB(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun save(userId: String, shoppingId: String, product: Product){
        getDB().collection(LISTS).document(userId).collection(shoppingId).add(product.map())
    }

    fun getLists(id: String): Task<DocumentSnapshot> {
        return getDB().collection(LISTS).document("1").get()
    }
    fun getProducts(id: String): CollectionReference {
        return getDB().collection(LISTS).document("1").collection("1")
    }

    fun save(adminId: String, shopping: Shopping): String {
        val ref = getDB().collection(LISTS).document(adminId)
        ref.set(shopping.map())
        return ref.id
    }

}