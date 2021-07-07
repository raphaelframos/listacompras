package com.powellapps.buyforme.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.powellapps.buyforme.R
import com.powellapps.buyforme.databinding.AdapterPersonBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

const val PRODUCT = "product"
class Utils {

    val COMPRA_PRA_MIM = "Comprapramim"

    fun show(s: String) {
       Log.v(COMPRA_PRA_MIM, s);
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

    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Long): String {
        val format = SimpleDateFormat("dd/MM/yy")
        return format.format(date)
    }

    fun setImage(binding: AdapterPersonBinding, picture: String) {
        Glide.with(binding.root)
            .load(picture)
            .transform(CircleCrop())
            .into(binding.imageViewPhoto)
    }

    fun alert(
        activity: Activity,
        title: String,
        message: String,
        yesListener: DialogInterface.OnClickListener,
        noListener: DialogInterface.OnClickListener?
    ) {
        val alert = AlertDialog.Builder(activity)
        alert.setTitle(title).setMessage(message).setPositiveButton(activity.getString(R.string.sim), yesListener).setNegativeButton(activity.getString(
                    R.string.nao), noListener).show()
    }

}