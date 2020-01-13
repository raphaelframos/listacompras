package com.powellapps.compraparamim.utils

import android.util.Log
import java.security.MessageDigest

class Utils {

    val COMPRA_PRA_MIM = "Comprapramim"

    fun show(s: String) {
        Log.v(COMPRA_PRA_MIM, s);
    }

    fun generateRandomPassword(): String {
        val STRING_CHARACTERS = ('0'..'z').toList().toTypedArray()
        val password = (1..4).map { STRING_CHARACTERS.random() }.joinToString("")
        show(password)
        return password.toLowerCase()
    }


}