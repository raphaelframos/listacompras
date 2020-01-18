package com.powellapps.compraparamim.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.powellapps.compraparamim.model.Share
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.model.User
import com.powellapps.compraparamim.ui.newlist.Product
import com.powellapps.compraparamim.utils.Utils


class FirebaseRepository {

    private val LISTS = "lists"
    private val PRODUCTS = "products"
    private val USERS = "users"
    private val USER_ID = "userId"

     fun getDB(): FirebaseFirestore {
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
        return query
    }

    fun getSharedShopping(sharedId: String, sharedPassword: String): Query {
        val query = getLists().whereEqualTo("shareId", sharedId).whereEqualTo("sharePassword", sharedPassword)
        return query
    }

    fun getSharedIds(userId: String): Query {
        val query = getShare().whereEqualTo("userId", userId)
        return query
    }


    fun getProducts(shoppingId: String): DocumentReference {
        return  getLists().document(shoppingId)
    }

    fun removeShopping(documentId: String) {
        getLists().document(documentId).delete()
    }


    fun getMostProducts(adminId: String): CollectionReference {
        return getDB().collection(USERS).document(adminId).collection(PRODUCTS)
    }

    fun getUserId(): String {
        return "1"
    }

    fun updateShare(shopping: Shopping) {
        getLists().document(shopping.documentId).update(shopping.shareMap())
    }

    fun removeProduct(shopping: Shopping) {
        getLists().document(shopping.documentId).update("products", shopping.products)
    }

    fun follow(userId: String, shopping: Shopping) {
        shopping.add(userId)
        getLists().document(shopping.documentId).update("shared", shopping.shared)
        val share = Share()
        share.shoppingId = shopping.documentId
        share.userId = userId
        getShare().add(share)
    }

    fun saveUser(user: User) {
        val userfirebase = FirebaseAuth.getInstance().currentUser
        val id = userfirebase!!.uid
        FirebaseRepository().getDB().collection("users").document(id).set(user)
    }

    private fun getShare() = getDB().collection("shared")


}