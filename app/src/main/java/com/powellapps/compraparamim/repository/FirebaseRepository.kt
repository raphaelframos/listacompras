package com.powellapps.compraparamim.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.ui.newlist.Product


class FirebaseRepository {

    private val LISTS = "lists"
    private val MY = "my"
    private val PRODUCTS = "products"
    private val USERS = "users"
    private val USER_ID = "userId"

    private fun getDB(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun getLists() =
        getDB().collection(LISTS)

    fun saveShopping(shopping: Shopping): String? {
        val ref = getLists().document()
        ref.set(shopping)
        return ref.id
    }

    fun saveProduct(shopping: Shopping, product: Product) {
        shopping.add(product)
        getLists().document(shopping.documentId).update("products", shopping.products)
        getDB().collection(USERS).document(shopping.userId).collection(PRODUCTS).add(product.nameMap())
    }

    fun getListsById(adminId: String): Query {
        val query = getLists().whereEqualTo(USER_ID, adminId)
     //   query.orderBy("name", Query.Direction.DESCENDING)
        return query
    }

    fun getProducts(shoppingId: String): DocumentReference {
        return  getLists().document(shoppingId)
    }




    fun remove(adminId: String, shoppingId : String, product: Product) {
        getLists(adminId).document(shoppingId).collection(PRODUCTS).document(product.documentId).delete()
    }

    fun getMostProducts(adminId: String): CollectionReference {
        return getDB().collection(USERS).document(adminId).collection(PRODUCTS)
    }

    fun getUserId(): String {
        return "1"
    }

    fun removeShopping(userId: String, documentId: String) {
        getLists(userId).document(documentId).delete()
    }

    fun getLists(adminId: String) =
        getDB().collection(LISTS).document(adminId).collection(MY)

    fun updateShare(id: String, shopping: Shopping) {
        getLists(id).document(shopping.documentId).update(shopping.shareMap())
    }


}