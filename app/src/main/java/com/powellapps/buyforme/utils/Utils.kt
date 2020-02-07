package com.powellapps.buyforme.utils

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import androidx.core.content.ContextCompat
import com.powellapps.buyforme.model.Shopping
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    val COMPRA_PRA_MIM = "Comprapramim"

    fun show(s: String) {
      //  Log.v(COMPRA_PRA_MIM, s);
    }

    fun generateRandomPassword(): String {
        val STRING_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzZ"
        val password = (1..3).map { STRING_CHARACTERS.random() }.joinToString("")
        return password.toLowerCase(Locale.getDefault())
    }

    fun formatDate(date: Date) : String {
        val format = SimpleDateFormat("dd/MM/yy")
        return format.format(date)
    }

    fun generateId(s: String): String {
        val id = (1..3).map { s.random() }.joinToString("")
        return id.toLowerCase()
    }

    fun Context.colorList(id: Int): ColorStateList {
        return ColorStateList.valueOf(ContextCompat.getColor(this, id))
    }

    fun getMoney(d: Double) : String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        return format.format(d)
    }

    fun maxName(list: List<Shopping>): Int {
        var number = 0
        list.forEach {
            if(it.name > number){
                number = it.name
            }
        }
        return number
    }


}