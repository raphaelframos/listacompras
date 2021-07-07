package com.powellapps.buyforme.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.powellapps.buyforme.R
import com.powellapps.buyforme.databinding.FragmentNewProductBinding
import com.powellapps.buyforme.model.Product
import com.powellapps.buyforme.viewmodel.ProductViewModel

class NewProductFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewProductBinding
    private lateinit var viewModel : ProductViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewProductBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configViewModel()
        configButton()
    }

    private fun configButton() {
        binding.buttonSaveProduct.setOnClickListener {
            save()
        }

        binding.editTextProductName.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                save()
            }
            false
        }
    }

    private fun save() {
        val name = binding.editTextProductName.text.toString()
        if (name.isNotEmpty()) {
            save(name)
            clean()
        } else {
            binding.editTextProductName.error = getString(R.string.campo_branco)
        }
    }

    private fun clean() {
        binding.editTextProductName.setText("")
        binding.spinnerAmount.setSelection(0)
    }

    private fun save(name: String) {
        val amount = binding.spinnerAmount.selectedItem.toString()
        val product = Product(name, amount)
        auth.currentUser?.let {
            product.userId = it.uid
            viewModel.save(product)
        }
    }

    private fun configViewModel() {
        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
    }

    override fun getTheme(): Int = R.style.BaseBottomSheetDialog
}