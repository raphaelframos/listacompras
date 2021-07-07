package com.powellapps.buyforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.buyforme.model.MostUsedProduct
import com.powellapps.buyforme.repository.FirebaseRepository
import com.powellapps.buyforme.model.Product
import com.powellapps.buyforme.model.Purchase
import com.powellapps.buyforme.repository.PURCHASED
import com.powellapps.buyforme.utils.Utils

class ProductViewModel : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    var products : LiveData<List<Product>> = _products


    fun save(product: Product) {
        FirebaseRepository().saveProduct(product)
    }

    fun getProducts(uid: String) {
        FirebaseRepository().getProducts(uid).orderBy(PURCHASED).addSnapshotListener { querySnapshot, _ ->
            querySnapshot?.let {
                _products.postValue(it.toObjects(Product::class.java))
            }
        }
    }

    fun update(purchase: Purchase, documentId: String, purchased: Boolean, userId: String) {
        FirebaseRepository().updatePurchased(purchase, documentId, purchased, userId)
    }

    fun remove(productId: String) {
        FirebaseRepository().getProducts().document(productId).delete()
    }
}