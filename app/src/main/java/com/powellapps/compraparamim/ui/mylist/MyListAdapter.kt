package com.powellapps.compraparamim.ui.mylist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.compraparamim.R

class MyListAdapter() : RecyclerView.Adapter<MyListAdapter.ViewHolder>() {

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

    }

    fun update(it: List<Shopping>) {
        this.shoppingList = it
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textViewNumber : TextView = itemView.findViewById(R.id.textView_number)
        val textViewDate : TextView = itemView.findViewById(R.id.textView_date)

        fun bind(shopping: Shopping) {
            textViewNumber.text = "#" + shopping.number
            textViewDate.text = "" + shopping.date
        }

    }
}