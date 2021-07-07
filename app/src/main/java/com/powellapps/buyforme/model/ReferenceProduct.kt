package com.powellapps.buyforme.model

import com.google.firebase.firestore.DocumentId

class ReferenceProduct {

    constructor()

    constructor(product: Product){
        name = product.name
        shoppingId = product.shoppingId
        userId = product.userId

    }

    var name = ""
    var shoppingId = ""
    var userId = ""
    @DocumentId
    var documentId = ""
    var prices = ArrayList<Double>()

    fun bestPrice(): Double {
        if(prices.size > 0 ) {
            return prices.min()!!
        }
        return 0.0
    }
    override fun toString(): String {
        return name
    }

}