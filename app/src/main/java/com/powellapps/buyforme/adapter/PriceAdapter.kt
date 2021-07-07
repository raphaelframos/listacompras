package com.powellapps.buyforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.buyforme.databinding.AdapterPriceBinding
import com.powellapps.buyforme.extensions.date
import com.powellapps.buyforme.extensions.money
import com.powellapps.buyforme.extensions.visibleOrGone
import com.powellapps.buyforme.model.Purchase

class PriceAdapter(private val purchases: ArrayList<Purchase>) :
    RecyclerView.Adapter<PriceAdapter.PriceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val binding = AdapterPriceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PriceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.bind(purchases[position])
    }

    override fun getItemCount()= purchases.size

    class PriceViewHolder(private val binding: AdapterPriceBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(purchase: Purchase) {
            binding.textViewDate.text = purchase.timestamp.date()
            binding.textViewPlace.text = purchase.place
            binding.textViewPlace.visibleOrGone(purchase.place.isNotEmpty())
            binding.textViewPrice.text = purchase.price.money()
            binding.textViewObservation.text = purchase.observation
        }
    }
}