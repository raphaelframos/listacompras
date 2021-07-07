package com.powellapps.buyforme.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.powellapps.buyforme.utils.Utils
import java.text.NumberFormat

fun View.gone() {
    visibility = GONE
}

fun View.visibleOrGone(isVisible: Boolean) {
    visibility = if(isVisible) VISIBLE else GONE
}

fun Double.money() : String {
    return try {
        NumberFormat.getCurrencyInstance().format(this.toDouble())
    }catch (e: Exception){
        ""
    }
}

fun Double.hasValue() = this > 0

fun Long.date() = Utils().formatDate(this)


