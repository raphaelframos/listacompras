package com.powellapps.compraparamim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.powellapps.compraparamim.adapter.ProductNameAdapter
import com.powellapps.compraparamim.adapter.MyListAdapter
import com.powellapps.compraparamim.fragment.SearchListFragment
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.viewmodel.MyListViewModel
import com.powellapps.compraparamim.viewmodel.ShareViewModel
import com.powellapps.compraparamim.utils.ConstantsUtils
import com.powellapps.compraparamim.utils.Utils
import com.powellapps.compraparamim.viewmodel.ProductViewModel

class ListsActivity : AppCompatActivity() {

    private lateinit var myListViewModel: MyListViewModel
    private lateinit var productViewModel : ProductViewModel
    private lateinit var shareViewModel : ShareViewModel
    private lateinit var adapter : MyListAdapter
    private var adapterShared =
        MyListAdapter(this, true)
    private var adapterProducts =
        ProductNameAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)

       // supportActionBar?.elevation = 0F
        adapter = MyListAdapter(this, false)

        myListViewModel = ViewModelProvider(this).get(MyListViewModel::class.java)
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        shareViewModel = ViewModelProvider(this).get(ShareViewModel::class.java)

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
            val it = Intent(this, NewListActivity::class.java)
            it.putExtra(ConstantsUtils.POSITION.name, Utils().maxName(adapter.shoppingList))
            startActivity(it)
        })

        if(FirebaseRepository().existUser()){
            myListViewModel.getList(FirebaseRepository().getUserId()).observe(this, Observer {
                adapter.update(it)
            })

            shareViewModel.getShareShoppings(FirebaseRepository().getUserId()).observe(this, Observer {
                adapterShared.update(it)
            })

            productViewModel.getProducts(FirebaseRepository().getUserId()).observe(this, Observer {
                adapterProducts.update(it)
            })
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){
            R.id.item_search -> {
                SearchListFragment().show(supportFragmentManager, "search")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
