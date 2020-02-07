package com.powellapps.buyforme

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.snackbar.Snackbar
import com.powellapps.buyforme.adapter.ProductAdapter
import com.powellapps.buyforme.fragment.ShareListFragment
import com.powellapps.buyforme.model.Product
import com.powellapps.buyforme.model.ReferenceProduct
import com.powellapps.buyforme.model.Shopping
import com.powellapps.buyforme.repository.FirebaseRepository
import com.powellapps.buyforme.utils.ConstantsUtils
import com.powellapps.buyforme.utils.Utils
import com.powellapps.buyforme.viewmodel.NewListViewModel
import com.powellapps.buyforme.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.activity_new_list.*


class NewListActivity : AppCompatActivity() {

    var adapter = ProductAdapter(this)
    lateinit var editTextName : AutoCompleteTextView
    var shopping : Shopping = Shopping()
    var product : Product = Product()
    private lateinit var mAdView: AdView

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

        MobileAds.initialize(this) {
            mAdView = findViewById(R.id.adView)
            val adRequest: AdRequest = AdRequest.Builder().build()
            mAdView.loadAd(adRequest)
        }


    //
    }

    private fun createProduct() {
        val name = editTextName.text.toString()
        if (name.isNotEmpty()) {
            val amount = spinner_amount.selectedItem as String
            product.name = name
            product.amount = amount.toInt()
            FirebaseRepository().saveProduct(shopping, product)
            product = Product()
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

        viewModelNames.getReferenceProducts(FirebaseRepository().getUserId()).observe(this, Observer {
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
        val viewModel = ViewModelProvider(this).get(NewListViewModel::class.java)
        val viewModelNames = ViewModelProvider(this).get(ProductViewModel::class.java)
        return Pair(viewModel, viewModelNames)
    }

    private fun setShopping() {
        if (isNew()) {
            val newPosition = intent.getIntExtra(ConstantsUtils.POSITION.name, 1) + 1
            shopping = Shopping()
            shopping.name = newPosition
            shopping.userId = FirebaseRepository().getUserId()
            shopping.userPhoto = FirebaseRepository().getUser().photoUrl.toString()
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
        value?.let {
            shopping = it as Shopping
        }
    }

    fun isNew() : Boolean {
        return intent.getSerializableExtra(ConstantsUtils.SHOPPING.name) == null
    }

    fun setNames(it: List<ReferenceProduct>) {
        var names = ArrayList<ReferenceProduct>()
        it.forEach {
            if(!names.contains(it)){
                names.add(it)
            }
        }
        val adapter = ArrayAdapter<ReferenceProduct>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            names
        )
        editTextName.setAdapter(adapter)
        editTextName.setOnItemClickListener{ adapter, view, position, l ->
            val referenceProduct : ReferenceProduct = (editTextName.adapter.getItem(position) as ReferenceProduct)
            product.referenceId = referenceProduct.documentId
            product.bestPrice = referenceProduct.bestPrice()
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
                    val products = adapter.products
                    val newPosition = intent.getIntExtra(ConstantsUtils.SHOPPING_NAME.name, 1) + 1
                    Utils().show("Teste " + newPosition)

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(adapter.products.size == 0) {
            FirebaseRepository().removeShopping(shopping.documentId)
        }
        super.onBackPressed()
    }
}
