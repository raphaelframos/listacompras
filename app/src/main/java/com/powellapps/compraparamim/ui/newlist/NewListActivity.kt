package com.powellapps.compraparamim.ui.newlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.utils.Utils

class NewListActivity : AppCompatActivity() {

    var adapter = ProductAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_list)

        val recyclerViewProducts = findViewById<RecyclerView>(R.id.recyclerView_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.adapter = adapter
        recyclerViewProducts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val viewModel = ViewModelProviders.of(this).get(ViewModelNewList::class.java)
        viewModel.getProducts("1").observe(this, Observer {
            adapter.update(it)
        })

        val imageButtonSend = findViewById<ImageButton>(R.id.imageButton_send)
        imageButtonSend.setOnClickListener({
            val editTextName : TextInputEditText = findViewById(R.id.textInputEditText_name)
            val name = editTextName.text.toString()
            val product = Product(name)
            FirebaseRepository().save(product)

        })
    }
}
