package com.powellapps.compraparamim.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.powellapps.compraparamim.ui.mylist.Shopping
import com.powellapps.compraparamim.ui.newlist.Product


class FirebaseRepository {

    private val LISTS = "lists"
    private val MY = "my"
    private val PRODUCTS = "products"
    private val USERS = "users"

    private fun getDB(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun getLists(id: String): CollectionReference {
        return getDB().collection(LISTS).document(id).collection(MY)
    }
    fun getProducts(id: String, shoppingId: String): CollectionReference {
        return getDB().collection(LISTS).document(id).collection(MY).document(shoppingId).collection(PRODUCTS)
    }

    fun save(adminId: String, shopping: Shopping): String? {
        val ref =  getDB().collection(LISTS).document(adminId).collection(MY).document()
        ref.set(shopping)
        return ref.id
    }

    fun save(adminId: String, shoppingId : String, product: Product) {
        getDB().collection(LISTS).document(adminId).collection(MY).document(shoppingId).collection(PRODUCTS).add(product)
        getDB().collection(USERS).document(adminId).collection(PRODUCTS).add(product.nameMap())
    }

    fun remove(adminId: String, shoppingId : String, product: Product) {
        getDB().collection(LISTS).document(adminId).collection(MY).document(shoppingId).collection(PRODUCTS).document(product.documentId).delete()
    }

    fun getMostProducts(adminId: String): CollectionReference {
        return getDB().collection(USERS).document(adminId).collection(PRODUCTS)
    }

    fun getUserId(): String {
        return "1"
    }


}