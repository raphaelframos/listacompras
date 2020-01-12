package com.powellapps.compraparamim.ui.mylist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.ui.home.HomeViewModel
import java.util.concurrent.ConcurrentNavigableMap

/**
 * A simple [Fragment] subclass.
 */
class MyListFragment : Fragment() {

    private lateinit var myListViewModel: MyListViewModel
    private var adapter = MyListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myListViewModel =
            ViewModelProviders.of(this).get(MyListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_list, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerView_myList)
     //   recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        var list = ArrayList<Shopping>()
        list.add(Shopping())
        list.add(Shopping())
        Toast.makeText(context, "Teste " + list.count(), Toast.LENGTH_LONG).show()
        adapter.update(list)

       // myListViewModel.list.observe(this, Observer {
         //   adapter.update(it)
        //})


        return root
    }


}
