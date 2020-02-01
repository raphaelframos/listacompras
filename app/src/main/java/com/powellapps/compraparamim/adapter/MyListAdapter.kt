package com.powellapps.compraparamim.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.NewListActivity
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.utils.ConstantsUtils
import com.powellapps.compraparamim.utils.Utils
import java.util.*

class MyListAdapter(val context: Context, val isShared : Boolean) : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

    var shoppingList: List<Shopping> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopping = shoppingList.get(position)
        holder.bind(shopping)
        holder.itemView.setOnClickListener({
            val it = Intent(context, NewListActivity::class.java)
            it.putExtra(ConstantsUtils.SHOPPING.name, shopping)
            it.putExtra(ConstantsUtils.SHOPPING_NAME.name, Utils().maxName(shoppingList))
            context.startActivity(it)
        })

        if(isShared){
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        }else{
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.green))
        }

        Glide.with(context)
            .load(shopping.userPhoto)
            .apply(RequestOptions.circleCropTransform())
            .into(holder.imageView);
    }

    fun update(it: List<Shopping>) {
        this.shoppingList = it
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewNumber : TextView = itemView.findViewById(R.id.textView_number)
        val textViewDate : TextView = itemView.findViewById(R.id.textView_date)
        var cardView : CardView = itemView.findViewById(R.id.cardView_my_list)
        var imageView : ImageView = itemView.findViewById(R.id.imageView_photo)

        fun bind(shopping: Shopping) {
            textViewNumber.text = shopping.nameFormat()
            textViewDate.text = Utils().formatDate(Date(shopping.date))

        }

    }
}