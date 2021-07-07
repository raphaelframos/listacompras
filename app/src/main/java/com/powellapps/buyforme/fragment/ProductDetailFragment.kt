package com.powellapps.buyforme.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.buyforme.R
import com.powellapps.buyforme.adapter.PriceAdapter
import com.powellapps.buyforme.databinding.FragmentProductDetailBinding
import com.powellapps.buyforme.model.Product
import com.powellapps.buyforme.model.Purchase
import com.powellapps.buyforme.utils.PRODUCT
import com.powellapps.buyforme.utils.Utils
import com.powellapps.buyforme.viewmodel.ProductViewModel

class ProductDetailFragment : DialogFragment() {

    private lateinit var product: Product
    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var viewModel : ProductViewModel
    private lateinit var adapter : PriceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        binding.toolbar.toolbar.title = getString(R.string.atualizando)
        arguments?.let {
            product = arguments?.getSerializable(PRODUCT) as Product
            binding.checkBoxProductPurchase.isChecked = product.purchased
            configButton(product)
            adapter = PriceAdapter(product.purchases)
            configRecycler()
            binding.toolbar.toolbar.inflateMenu(R.menu.detail_menu)
            binding.toolbar.toolbar.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.item_delete ->
                        Utils().alert(requireActivity(), getString(R.string.atencao), getString(R.string.deseja_remover_produto), deleteProductListener, null)
                }
                true
            }
        }
    }

    private val deleteProductListener = { _: DialogInterface, _: Int ->
        viewModel.remove(product.documentId)
        dismiss()
    }

    private fun configRecycler() {
        binding.recyclerViewPriceHistory.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPriceHistory.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.HORIZONTAL
            )
        )
        binding.recyclerViewPriceHistory.adapter = adapter
    }

    private fun configButton(product: Product) {
        binding.buttonAddPrice.setOnClickListener {
            val purchase = getPurchase()
            val purchased = binding.checkBoxProductPurchase.isChecked
            viewModel.update(purchase, product.documentId, purchased, product.userId)
            dismiss()
        }
    }

    private fun getPurchase(): Purchase {
        val price = getPrice()
        val place = binding.editTextPlace.text.toString()
        val observation = binding.editTextProductObservation.text.toString()
        return Purchase(price, place, observation)
    }

    private fun getPrice(): Double {
        val textValue = binding.editTextNewPrice.text.toString()
        if(textValue.isEmpty()){
            return 0.0
        }
        return textValue.toDouble()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.window
                ?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
                )
        }
    }

    companion object {
        fun newInstance(product : Product): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val bundle = Bundle()
            bundle.putSerializable(PRODUCT, product)
            fragment.arguments = bundle
            return fragment
        }
    }

}