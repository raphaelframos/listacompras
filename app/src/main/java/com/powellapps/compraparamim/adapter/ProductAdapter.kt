package com.powellapps.compraparamim.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.ui.newlist.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var products = ArrayList<Product>()
    lateinit var shopping: Shopping

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnLongClickListener{
            products.removeAt(position)
            shopping.products = products
            FirebaseRepository().removeProduct(shopping)
            notifyItemChanged(position)
            true
        }

        holder.checkBoxBought.setOnCheckedChangeListener{ button, isChecked ->
            products.removeAt(position)
            product.purchased = isChecked
            products.add(product)
            shopping.products = products
            FirebaseRepository().updateProducts(shopping)

        }
    }

    fun update(it: List<Product>, shopping: Shopping) {
        this.products = ArrayList(it)
        this.shopping = shopping
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val textViewName : TextView = itemView.findViewById(R.id.textView_name)
        val textViewAmount : TextView = itemView.findViewById(R.id.textView_amount)
        val checkBoxBought : CheckBox = itemView.findViewById(R.id.checkBox_bought)

        fun bind(product: Product) {
            textViewAmount.text = ""+product.amount
            textViewName.text = product.name
            checkBoxBought.isChecked = product.purchased
        }

    }
}