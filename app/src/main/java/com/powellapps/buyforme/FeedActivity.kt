package com.powellapps.buyforme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.powellapps.buyforme.adapter.FeedAdapter
import com.powellapps.buyforme.databinding.ActivityFeedBinding
import com.powellapps.buyforme.fragment.NewProductFragment
import com.powellapps.buyforme.fragment.ProductDetailFragment
import com.powellapps.buyforme.model.Product
import com.powellapps.buyforme.repository.FirebaseRepository
import com.powellapps.buyforme.viewmodel.ProductViewModel

class FeedActivity : AppCompatActivity(), FeedAdapter.OnFeedClickListener {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var adapter: FeedAdapter
    private lateinit var viewModel : ProductViewModel
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(LayoutInflater.from(this))
        firebaseAuth = FirebaseAuth.getInstance()
        configRecyclerView()
        configViewModel()
        configButton()
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        setContentView(binding.root)
    }

    private fun configButton() {
        binding.floatingActionButtonNewProduct.setOnClickListener {
            if(FirebaseRepository().existUser()){
                NewProductFragment().show(supportFragmentManager, getString(R.string.tag_new_product))
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun configViewModel() {
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        firebaseAuth.let { auth ->
            auth.currentUser?.let {
                viewModel.getProducts(it.uid)
            }
        }
        viewModel.products.observe(this, { products ->
            adapter.update(products)
        })
    }

    private fun configRecyclerView() {
        adapter = FeedAdapter(this)
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewProducts.adapter = adapter
        binding.recyclerViewProducts.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun click(product: Product) {
        ProductDetailFragment.newInstance(product).show(supportFragmentManager, getString(R.string.tag_product_detail))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.item_shared -> startActivity(Intent(this, PersonActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}