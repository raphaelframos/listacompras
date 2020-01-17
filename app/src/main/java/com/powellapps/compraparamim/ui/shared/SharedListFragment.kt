package com.powellapps.compraparamim.ui.shared

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.powellapps.compraparamim.R

class SharedListFragment : Fragment() {

    companion object {
        fun newInstance() = SharedListFragment()
    }

    private lateinit var viewModel: SharedListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SharedListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
