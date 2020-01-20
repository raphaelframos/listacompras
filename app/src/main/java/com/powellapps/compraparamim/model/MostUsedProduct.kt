package com.powellapps.compraparamim.model

import com.powellapps.compraparamim.utils.Utils

class MostUsedProduct(
    name: String,
    group: List<Product>
) {

    var name = name
    var list = group.sortedWith(compareBy({it.date}))
    var referenceId = group.get(0).documentId

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MostUsedProduct

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }


}