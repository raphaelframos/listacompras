package com.powellapps.compraparamim.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.powellapps.compraparamim.model.*
import com.powellapps.compraparamim.utils.Utils


class FirebaseRepository {

    private val REFERENCES: String = "references"
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
        product.shoppingId = shopping.documentId
        product.userId = shopping.userId
        saveReference(product)




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

    private fun saveReference(product: Product) {
        if(product.referenceId.isEmpty()){
            Utils().show("Novo produto")
            createReference(product)
        }else{
            Utils().show("Tenho referencia")
            getReferences(product.userId).whereEqualTo("name", product.name).addSnapshotListener { querySnapshot, _ ->
                if(querySnapshot?.size() == 0){
                    createReference(product)
                }else{
                    val reference = querySnapshot!!.documents.get(0).toObject(ReferenceProduct::class.java)
                    Utils().show("Quantidade de documtnso " + querySnapshot.documents.size)
                    product.referenceId = reference!!.documentId
                    product.bestPrice = reference.bestPrice()
                    Utils().show("Criando produto " + product.documentId)
                    getDB().collection(PRODUCTS).add(product)
                }
            }
        }
    }

    private fun createReference(product: Product) {
        var reference = ReferenceProduct(product)
        getReferences(product.userId).add(reference).addOnSuccessListener {
            product.referenceId = it.id
            getDB().collection(PRODUCTS).add(product)
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
        return getUser().uid
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

    fun existUser() : Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun getUser(): FirebaseUser {
        if(existUser()) {
            return FirebaseAuth.getInstance().currentUser!!
        }
        throw NoClassDefFoundError()
    }

    private fun getShare() = getDB().collection("shared")

    private fun getProducts() = getDB().collection(PRODUCTS)

    fun getProductsBy(shoppingId: String): Query {
        return getProducts().whereEqualTo("shoppingId", shoppingId)
    }

    fun updatePurchased(product: Product): Task<Void> {
        return getProducts().document(product.documentId).update(product.purchasedMap())
    }

    fun getProduct(id: String): DocumentReference {
        return getProducts().document(id)
    }

    fun updateNewPrice(product: Product) {

        getReferences(product.userId).document(product.referenceId).update("prices", FieldValue.arrayUnion(product.currentPrice)).addOnSuccessListener {
            Utils().show("succes")
            getReferences(product.userId).document(product.referenceId).get().continueWithTask {
                val reference = it.result!!.toObject(ReferenceProduct::class.java)
                product.bestPrice = reference!!.bestPrice()
                Utils().show("Salvando produto com novo preço " + product.documentId + " com " + product.bestPrice)
                getProduct(product.documentId).update(product.newPriceMap())

            }


        }
    }

    fun getReferenceProducts(userId: String): CollectionReference {
        return getReferences(userId)
    }

    private fun getReferences(userId: String): CollectionReference {
        return getDB().collection(REFERENCES).document(userId).collection(PRODUCTS)
    }

}