package com.powellapps.buyforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.buyforme.R
import com.powellapps.buyforme.databinding.AdapterFeedBinding
import com.powellapps.buyforme.extensions.hasValue
import com.powellapps.buyforme.extensions.money
import com.powellapps.buyforme.extensions.visibleOrGone
import com.powellapps.buyforme.model.Product
import kotlinx.android.synthetic.main.adapter_feed.view.*

class FeedAdapter(private val listener: OnFeedClickListener) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private var products : List<Product> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = AdapterFeedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            listener.click(product)
        }
        holder.itemView.check_box_purchase.setOnClickListener {
            listener.click(product)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun update(list: List<Product>) {
        this.products = list
        this.notifyDataSetChanged()
    }

    inner class FeedViewHolder(private val binding: AdapterFeedBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(product: Product) {
            val bestPrice = product.bestPrice()
            binding.textViewProductBestPrice.text = bestPrice.money()
            binding.textViewProductBestPrice.visibleOrGone(bestPrice.hasValue())
            binding.textViewProductName.text = product.name
            binding.textViewProductAmount.text = product.amount.toString()
            binding.checkBoxPurchase.isChecked = product.purchased
            binding.textViewProductName.setTextColor(getColor(binding, product.purchased))
        }

        private fun getColor(binding: AdapterFeedBinding, isPurchased : Boolean) : Int =
            if (isPurchased) ContextCompat.getColor(binding.root.context, R.color.gray) else ContextCompat.getColor(binding.root.context, R.color.dark_gray)
    }

    interface OnFeedClickListener{
        fun click(product: Product)
    }
}