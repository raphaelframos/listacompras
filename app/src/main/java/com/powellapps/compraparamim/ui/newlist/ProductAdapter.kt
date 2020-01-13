package com.powellapps.compraparamim.ui.newlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.compraparamim.R

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    var products = emptyList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    fun update(it: List<Product>) {
        this.products = it
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