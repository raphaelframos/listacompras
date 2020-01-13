package com.powellapps.compraparamim.ui.newlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.ui.mylist.Shopping
import com.powellapps.compraparamim.utils.ConstantsUtils
import java.util.*
import kotlin.collections.ArrayList

class NewListActivity : AppCompatActivity() {

    var adapter = ProductAdapter()
    lateinit var editTextName : AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_list)

        editTextName = findViewById(R.id.textInputEditText_name)

        val newPosition = intent.getIntExtra(ConstantsUtils.POSITION.name, 1) + 1
        val name : String = "#" + newPosition.toString()
        supportActionBar?.title = name
        val recyclerViewProducts = findViewById<RecyclerView>(R.id.recyclerView_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.adapter = adapter
        recyclerViewProducts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val viewModel = ViewModelProviders.of(this).get(ViewModelNewList::class.java)
        viewModel.getProducts("1").observe(this, Observer {
            adapter.update(it)
            setNames(it)
        })

        val shopping = Shopping()
        shopping.adminUser = "1"
        shopping.date = Date().time
        shopping.name = name

        val shoppingId = FirebaseRepository().save("1", shopping)

        val imageButtonSend = findViewById<ImageButton>(R.id.imageButton_send)
        imageButtonSend.setOnClickListener({

            val name = editTextName.text.toString()
            val product = Product(name)
            FirebaseRepository().save("1", shoppingId, product)
            editTextName.setText("")

        })
    }

    fun setNames(it: List<Product>) {
        var names = ArrayList<String>()
        it.forEach {
            if(!names.contains(it.name)){
                names.add(it.name)
            }
        }
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            names
        )
        editTextName.setAdapter(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item != null) {
            when(item.itemId){
                R.id.item_share -> {
                    ShareListFragment.newInstance("1").show(supportFragmentManager, "share")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
