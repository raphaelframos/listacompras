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
import com.powellapps.compraparamim.model.Product
import com.powellapps.compraparamim.fragment.ShareListFragment
import com.powellapps.compraparamim.model.MostUsedProduct
import com.powellapps.compraparamim.viewmodel.NewListViewModel
import com.powellapps.compraparamim.utils.ConstantsUtils
import com.powellapps.compraparamim.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_new_list.*
import kotlin.collections.ArrayList

class NewListActivity : AppCompatActivity() {

    var adapter = ProductAdapter(this)
    lateinit var editTextName : AutoCompleteTextView
    var shopping : Shopping = Shopping()
    var referenceId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_list)

        editTextName = findViewById(R.id.textInputEditText_name)
        initRecyclerView()

        val (viewModel, viewModelNames) = initViewModels()
        setShopping()
        supportActionBar?.title = shopping.nameFormat()
        getProducts(viewModel, viewModelNames)

        val imageButtonSend = findViewById<ImageButton>(R.id.imageButton_send)
        imageButtonSend.setOnClickListener({
            createProduct()
        })

    }

    private fun createProduct() {
        val name = editTextName.text.toString()
        if (name.isNotEmpty()) {
            val amount = spinner_amount.selectedItem as String
            val product = Product(name)
            product.amount = amount.toInt()
            product.referenceId = referenceId
            FirebaseRepository().saveProduct(
                shopping,
                product
            )
            editTextName.setText("")
        } else {
            editTextName.setError(getString(R.string.campo_branco))
        }
    }

    private fun getProducts(
        viewModel: NewListViewModel,
        viewModelNames: ProductViewModel
    ) {
        viewModel.getProducts(shopping.documentId).observe(this, Observer {
            adapter.update(it, shopping)
        })

        viewModelNames.getProducts(FirebaseRepository().getUserId()).observe(this, Observer {
            setNames(it)
        })
    }

    private fun initRecyclerView() {
        val recyclerViewProducts = findViewById<RecyclerView>(R.id.recyclerView_products)
        recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewProducts.adapter = adapter
        recyclerViewProducts.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun initViewModels(): Pair<NewListViewModel, ProductViewModel> {
        val viewModel = ViewModelProviders.of(this).get(NewListViewModel::class.java)
        val viewModelNames = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        return Pair(viewModel, viewModelNames)
    }

    private fun setShopping() {
        if (isNew()) {
            val newPosition = intent.getIntExtra(ConstantsUtils.POSITION.name, 1) + 1
            shopping = Shopping()
            shopping.name = newPosition
            shopping.userId = FirebaseRepository().getUserId()
            val id = FirebaseRepository().saveShopping(shopping)
            if (id != null) {
                shopping.documentId = id
            }
        } else {
            getShoppingIfExists()
        }
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

    fun setNames(it: List<MostUsedProduct>) {
        var names = ArrayList<MostUsedProduct>()
        it.forEach {
            if(!names.contains(it)){
                names.add(it)
            }
        }
        val adapter = ArrayAdapter<MostUsedProduct>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            names
        )
        editTextName.setAdapter(adapter)
        editTextName.setOnItemClickListener{ adapter, view, position, l ->
            val product : MostUsedProduct = (editTextName.adapter.getItem(position) as MostUsedProduct)
            referenceId = product.referenceId
            createProduct()
        }

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
                R.id.item_copy -> {

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(adapter.products.size == 0) {
            FirebaseRepository().removeShopping(
                shopping.documentId
            )
        }
        super.onBackPressed()
    }
}
