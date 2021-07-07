package com.powellapps.buyforme

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.powellapps.buyforme.adapter.PersonAdapter
import com.powellapps.buyforme.databinding.ActivityPersonBinding
import com.powellapps.buyforme.model.User
import com.powellapps.buyforme.utils.Utils
import com.powellapps.buyforme.viewmodel.UserViewModel

const val SHARED = 0
class PersonActivity : AppCompatActivity(), PersonAdapter.PersonListener {

    private lateinit var binding: ActivityPersonBinding
    private lateinit var viewModel : UserViewModel
    private lateinit var adapter : PersonAdapter
    private var listShared = emptyList<User>()
    private var listPerson = emptyList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonBinding.inflate(LayoutInflater.from(this))
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        adapter = PersonAdapter(emptyList(), this)

        binding.recyclerViewPersons.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewPersons.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.recyclerViewPersons.adapter = adapter

        viewModel.getUsers().observe(this, {
            listPerson = it
        })

        viewModel.getShareds().observe(this, {
            listShared = it
        })

        binding.tabLayoutOptions.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    update(it.position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        setContentView(binding.root)
    }

    fun update(position: Int) = if(position == SHARED) adapter.update(listShared) else adapter.update(listPerson)

    override fun update(user: User) {
        Utils().alert(this, getString(R.string.atencao), getString(R.string.deseja_compartilhar_lista) + " " + user.name + "?", shared(user), null)
    }

    private fun shared(user: User) = { _: DialogInterface, _: Int ->
        viewModel.shared(user, binding.tabLayoutOptions.selectedTabPosition)
    }
}