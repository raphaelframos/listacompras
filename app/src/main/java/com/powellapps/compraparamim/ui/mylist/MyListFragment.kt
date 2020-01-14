package com.powellapps.compraparamim.ui.mylist


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.ui.newlist.NewListActivity

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
        recyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter


        myListViewModel.getList("1").observe(this, Observer {
            adapter.update(it)
        })

        val floatingABNew : FloatingActionButton = root.findViewById(R.id.floatingActionButton_new)
        floatingABNew.setOnClickListener({
            val it = Intent(context, NewListActivity::class.java)
            startActivity(it)
        })

        return root
    }


}
