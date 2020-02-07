package com.powellapps.buyforme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.powellapps.buyforme.adapter.ProductNameAdapter
import com.powellapps.buyforme.adapter.MyListAdapter
import com.powellapps.buyforme.fragment.SearchListFragment
import com.powellapps.buyforme.repository.FirebaseRepository
import com.powellapps.buyforme.viewmodel.MyListViewModel
import com.powellapps.buyforme.viewmodel.ShareViewModel
import com.powellapps.buyforme.utils.ConstantsUtils
import com.powellapps.buyforme.utils.Utils
import com.powellapps.buyforme.viewmodel.ProductViewModel

class ListsActivity : AppCompatActivity() {

    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
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

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
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

    }

    override fun onResume() {
        super.onResume()
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

    override fun onBackPressed() {
        AlertDialog.Builder(this).setTitle("Atenção").setMessage("Deseja sair do app?").setPositiveButton("Sim",
            { dialog, which -> super.onBackPressed() }).setNegativeButton("Não", null).show()
    }
}
