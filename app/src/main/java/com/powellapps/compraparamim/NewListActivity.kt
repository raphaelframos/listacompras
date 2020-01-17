package com.powellapps.compraparamim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.powellapps.compraparamim.adapter.ProductAdapter
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.ui.newlist.Product
import com.powellapps.compraparamim.fragment.ShareListFragment
import com.powellapps.compraparamim.ui.newlist.ViewModelNewList
import com.powellapps.compraparamim.utils.ConstantsUtils
import com.powellapps.compraparamim.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_new_list.*
import kotlin.collections.ArrayList

class NewListActivity : AppCompatActivity() {

    var adapter = ProductAdapter()
    lateinit var editTextName : AutoCompleteTextView
    var shopping : Shopping =
        Shopping()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_list)

        editTextName = findViewById(R.id.textInputEditText_name)
        val recyclerViewProducts = findViewById<RecyclerView>(R.id.recyclerView_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.adapter = adapter
        recyclerViewProducts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        val viewModel = ViewModelProviders.of(this).get(ViewModelNewList::class.java)
        val viewModelNames = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        if(isNew()){
            val newPosition = intent.getIntExtra(ConstantsUtils.POSITION.name, 1) + 1
            shopping = Shopping()
            shopping.name = newPosition
            shopping.userId = FirebaseRepository().getUserId()
            val id = FirebaseRepository().saveShopping(shopping)
            if (id != null) {
                shopping.documentId = id
            }
        }else{
            getShoppingIfExists()
            viewModel.getProducts(shoppingId = shopping.documentId).observe(this, Observer {
                adapter.update(it, shopping)
            })
        }


        supportActionBar?.title = shopping.nameFormat()
        viewModel.getProducts(shopping.documentId).observe(this, Observer {
            adapter.update(it, shopping)
        })

        viewModelNames.getProducts(FirebaseRepository().getUserId()).observe(this, Observer {
            setNames(it)
        })

        val imageButtonSend = findViewById<ImageButton>(R.id.imageButton_send)
        imageButtonSend.setOnClickListener({

            val name = editTextName.text.toString()
            if(name.isNotEmpty()) {
                val amount = spinner_amount.selectedItem as String
                val product = Product(name)
                product.amount = amount.toInt()
                FirebaseRepository().saveProduct(
                    shopping,
                    product
                )
                editTextName.setText("")
            }else{
                editTextName.setError(getString(R.string.campo_branco))
            }

        })

    }

    fun showSnackbar(){
        val snack = Snackbar.make(editTextName, "O produto " + "foi removido. Deseja cancelar a exclusão?", Snackbar.LENGTH_LONG)
        snack.setAction(getString(R.string.cancelar), View.OnClickListener {
            System.out.println("Snackbar Set Action - OnClick.")
        })
        snack.show()
    }

    private fun getShoppingIfExists() {

        val value = intent.getSerializableExtra(ConstantsUtils.SHOPPING.name)
        value.let {
            shopping = it as Shopping
        }
    }

    fun isNew() : Boolean {
        return intent.getSerializableExtra(ConstantsUtils.SHOPPING.name) == null
    }

    fun setNames(it: List<String>) {
        var names = ArrayList<String>()
        it.forEach {
            if(!names.contains(it)){
                names.add(it)
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
                    ShareListFragment.newInstance(
                        FirebaseRepository().getUserId(), shopping
                    ).show(supportFragmentManager, "share")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(shopping.products.size == 0) {
            FirebaseRepository().removeShopping(
                shopping.documentId
            )
        }
        super.onBackPressed()
    }
}
