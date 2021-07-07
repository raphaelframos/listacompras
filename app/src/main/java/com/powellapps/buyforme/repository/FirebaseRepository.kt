package com.powellapps.buyforme.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.powellapps.buyforme.model.*
import com.powellapps.buyforme.utils.Utils

const val PURCHASED = "purchased"
private const val PURCHASES = "purchases"
private const val REFERENCES: String = "references"
private const val LISTS = "lists"
private const val PRODUCTS = "products"
private const val USERS = "users"
private const val USER_ID = "userId"
private const val SHAREDS = "shareds"
const val SHARED = "shared"
private const val FEED = "feed"
const val NAME = "name"

class FirebaseRepository {



     fun getDB(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    fun getLists() =
        getDB().collection(LISTS)

    fun saveEmail(shopping: Shopping, email: String){
        getLists().document(shopping.documentId).collection(SHAREDS).add(email)
    }

    fun saveProduct(shopping: Shopping, product: Product) {
        product.shoppingId = shopping.documentId
        product.userId = shopping.userId
    }



    private fun createReference(product: Product) {
        getReferences().add(product).addOnSuccessListener {
            product.referenceId = it.id
            getLists().document(product.shoppingId).collection(PRODUCTS).add(product)
        }
    }

    fun getListsById(adminId: String): Query {
        val query = getLists().whereEqualTo(USER_ID, adminId)
        return query
    }

    fun getSharedShopping(sharedId: String): Query {
        val query = getLists().whereEqualTo("shareId", sharedId)
        return query
    }

    fun getSharedIds(userId: String): Query {
        val query = getShared().whereEqualTo("userId", userId)
        return query
    }


    fun getShopping(shoppingId: String): DocumentReference {
        return  getLists().document(shoppingId)
    }


    private fun getUserId(): String {
        return getUser().uid
    }

    fun removeProduct(product: Product) {
        getProducts(product.userId).document(product.documentId).delete()
    }



    fun saveUser(user: User) {
        FirebaseRepository().getDB().collection("users").document(user.id).set(user)
    }

    fun existUser() : Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getUser(): FirebaseUser {
        if(existUser()) {
            return FirebaseAuth.getInstance().currentUser!!
        }
        throw NoClassDefFoundError()
    }


    fun getProducts(userId: String) = getDB().collection(FEED).document(userId).collection(PRODUCTS)

    fun getProducts(): CollectionReference {
        return getDB().collection(FEED).document(getUserId()).collection(PRODUCTS)
    }

    fun getReferences(): CollectionReference {
        return getDB().collection(REFERENCES)
    }

    fun getUsers(): CollectionReference {
        return getDB().collection(USERS);
    }

    fun getSharedsByUser(): CollectionReference {
        return getDB().collection(USERS).document(getUserId()).collection(SHARED)
    }

    fun addUserInShopping(user: User) {
        getLists().document(user.id).set(user)
    }

    fun saveProduct(p: Product) {
        getDB().collection(FEED).document(p.userId).collection(PRODUCTS).add(p)
    }

    fun updatePurchased(purchase: Purchase, documentId: String, purchased: Boolean, userId: String) {
        val map = HashMap<String, Any>()
        map[PURCHASED] = purchased
        if(purchase.price > 0) {
            FirebaseRepository().getProducts(userId).document(documentId)
                .update(PURCHASES, FieldValue.arrayUnion(purchase))
        }
        FirebaseRepository().getProducts(userId).document(documentId).update(map)
    }

    fun getShared() = getUsers().document(getUserId()).collection(SHARED)

    fun getShared(user: User) = getUsers().document(getUserId()).collection(SHARED).document(user.id)

    fun updateShare() = getUsers().document(getUserId()).collection(SHARED)

}