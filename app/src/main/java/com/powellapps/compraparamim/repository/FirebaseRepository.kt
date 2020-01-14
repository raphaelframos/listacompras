package com.powellapps.compraparamim.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.powellapps.compraparamim.ui.mylist.Shopping
import com.powellapps.compraparamim.ui.newlist.Product


class FirebaseRepository {

    private val LISTS = "lists"
    private val MY = "my"

    private fun getDB(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun getLists(id: String): CollectionReference {
        return getDB().collection(LISTS).document(id).collection(MY)
    }
    fun getProducts(id: String, shoppingId: String): CollectionReference {
        return getDB().collection(LISTS).document(id).collection(MY).document(shoppingId).collection("products")
    }

    fun save(adminId: String, shopping: Shopping): String? {
        val ref =  getDB().collection(LISTS).document(adminId).collection(MY).document()
        ref.set(shopping)
        return ref.id
    }

    fun save(adminId: String, shoppingId : String, product: Product) {
        getDB().collection(LISTS).document(adminId).collection(MY).document(shoppingId).collection("products").add(product)
    }

}