package com.powellapps.compraparamim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.powellapps.compraparamim.adapter.ProductNameAdapter
import com.powellapps.compraparamim.adapter.MyListAdapter
import com.powellapps.compraparamim.ui.mylist.MyListViewModel
import com.powellapps.compraparamim.ui.newlist.NewListActivity
import com.powellapps.compraparamim.utils.ConstantsUtils
import com.powellapps.compraparamim.viewmodel.ProductViewModel

class ListsActivity : AppCompatActivity() {

    private lateinit var myListViewModel: MyListViewModel
    private lateinit var productViewModel : ProductViewModel
    private lateinit var adapter : MyListAdapter
    private var adapterShared =
        MyListAdapter(this)
    private var adapterProducts =
        ProductNameAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)

       // supportActionBar?.elevation = 0F
        adapter = MyListAdapter(this)

        myListViewModel = ViewModelProviders.of(this).get(MyListViewModel::class.java)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView_myList)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        val recyclerViewShared: RecyclerView = findViewById(R.id.recyclerView_sharedList)
        recyclerViewShared.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewShared.adapter = adapterShared

        val recyclerViewProducts: RecyclerView = findViewById(R.id.recyclerView_products_most_used_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerViewProducts.adapter = adapterProducts

        val floatingNew : FloatingActionButton = findViewById(R.id.floatingActionButton_new_list)
        floatingNew.setOnClickListener({
            val position = adapter.shoppingList.size
            val it = Intent(this, NewListActivity::class.java)
            it.putExtra(ConstantsUtils.POSITION.name, position)
            startActivity(it)
        })

        myListViewModel.getList("1").observe(this, Observer {
            adapter.update(it)
        })

        myListViewModel.getList("1").observe(this, Observer {
            adapterShared.update(it)
        })

        productViewModel.getProducts("1").observe(this, Observer {
            adapterProducts.update(it)
        })
    }
}
