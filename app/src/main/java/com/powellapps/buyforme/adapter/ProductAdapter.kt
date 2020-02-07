package com.powellapps.buyforme.adapter

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.buyforme.R
import com.powellapps.buyforme.repository.FirebaseRepository
import com.powellapps.buyforme.model.Shopping
import com.powellapps.buyforme.model.Product
import com.powellapps.buyforme.utils.Utils

class ProductAdapter(val context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

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
            FirebaseRepository().removeProduct(product)
            true
        }

        if(product.purchased){
            holder.textViewName.setTextColor(ContextCompat.getColor(context, R.color.gray))
            holder.checkBoxBought.isChecked = true
        }else{
            holder.textViewName.setTextColor(ContextCompat.getColor(context, android.R.color.black))
            holder.checkBoxBought.isChecked = false
        }

        holder.itemView.setOnClickListener {
            product.purchased = !product.purchased

            if(product.purchased){
                showAlert(product)
            }else{
                updatePurchased(product)
            }

        }

    }

    fun showAlert(product: Product) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Preço")
        builder.setMessage("Qual valor você está pagando?")
        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_edit_text, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText_price)
        editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(dialogLayout)
        builder.setPositiveButton("Salvar"){ dialog, _ ->
            val value = editText.text.toString()
            if(value.isNotEmpty()){
                val price = value.toDouble()
                product.currentPrice = price
                Utils().show("Update new price")
                FirebaseRepository().updateNewPrice(product)
            }
            dialog.cancel()
        }

        builder.setNegativeButton("Cancelar"){ dialog, _ ->
            updatePurchased(product)
            dialog.cancel()
        }
        builder.create().show()

    }

    private fun updatePurchased(product: Product) {
        FirebaseRepository().updatePurchased(product)
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
        val textViewCurrent : TextView = itemView.findViewById(R.id.textView_current_price)
        val textViewBestPrice : TextView = itemView.findViewById(R.id.textView_best_price)

        fun bind(product: Product) {
            textViewAmount.text = ""+product.amount
            textViewName.text = product.name
            checkBoxBought.isChecked = product.purchased
            if(product.currentPrice > 0){
                textViewCurrent.text = Utils().getMoney(product.currentPrice)
                textViewCurrent.visibility = View.VISIBLE
            }else{
                textViewCurrent.visibility = View.GONE
            }
            if(product.bestPrice > 0){
                textViewBestPrice.text = Utils().getMoney(product.bestPrice)
                textViewBestPrice.visibility = View.VISIBLE
            }else{
                textViewBestPrice.visibility = View.GONE
            }


        }

    }
}