package com.powellapps.compraparamim.utils

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class Utils {

    val COMPRA_PRA_MIM = "Comprapramim"

    fun show(s: String) {
        Log.v(COMPRA_PRA_MIM, s);
    }

    fun generateRandomPassword(): String {
        val STRING_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzZ"
        val password = (1..4).map { STRING_CHARACTERS.random() }.joinToString("")
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


}