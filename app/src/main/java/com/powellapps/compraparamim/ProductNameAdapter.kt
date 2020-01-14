package com.powellapps.compraparamim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductNameAdapter : RecyclerView.Adapter<ProductNameAdapter.ViewHolder>() {

    private var list = emptyList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_product_name, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun update(it: List<String>?) {
        if (it != null) {
            this.list = it
            notifyDataSetChanged()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var textViewName = itemView.findViewById<TextView>(R.id.textView_name)
        fun bind(name: String) {
            textViewName.text = name
        }


    }
}