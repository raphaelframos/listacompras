package com.powellapps.compraparamim.repository

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
        Utils().show("Criando shopping")
        return ref.id
    }

    fun saveProduct(shopping: Shopping, product: Product) {
        product.shoppingId = shopping.documentId
        product.userId = shopping.userId
        getDB().collection(PRODUCTS).add(product)


     //   shopping.add(product)
     //   getLists().document(shopping.documentId).update("products", shopping.products)
    //    getDB().collection(USERS).document(shopping.userId).collection(PRODUCTS).add(product)
        /*
        val writeBatch: WriteBatch = rootRef.batch()
        batch.set(productsRef.document(), yourObject)
        batch.set(newProductsRef.document(), yourObject)

// This how you commit the batch
        // This how you commit the batch
        writeBatch.commit().addOnCompleteListener {
            // ...
        }

         */
    }

    fun getProduct(userId : String, name : String): Query {
        return getDB().collection(USERS).document(userId).collection(PRODUCTS).whereEqualTo("name", name)
    }

    fun updateProducts(shopping: Shopping) {
        getLists().document(shopping.documentId).update(PRODUCTS, shopping.products)
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


    fun getShopping(shoppingId: String): DocumentReference {
        return  getLists().document(shoppingId)
    }

    fun removeShopping(documentId: String) {
        getLists().document(documentId).delete()
    }


    fun getMostProducts(adminId: String): Query {
        return getDB().collection(PRODUCTS).whereEqualTo("userId", adminId)
    }

    fun getUserId(): String {
        return "2"
    }

    fun updateShare(shopping: Shopping) {
        getLists().document(shopping.documentId).update(shopping.shareMap())
    }

    fun removeProduct(product: Product) {
        getProducts().document(product.documentId).delete()
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

    private fun getProducts() = getDB().collection(PRODUCTS)

    fun getProductsBy(shoppingId: String): Query {
        return getProducts().whereEqualTo("shoppingId", shoppingId)
    }


}