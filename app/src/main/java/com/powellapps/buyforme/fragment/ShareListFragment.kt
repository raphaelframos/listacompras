package com.powellapps.buyforme.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.powellapps.buyforme.R
import com.powellapps.buyforme.model.Shopping
import com.powellapps.buyforme.model.User
import com.powellapps.buyforme.repository.FirebaseRepository
import com.powellapps.buyforme.utils.ConstantsUtils
import com.powellapps.buyforme.viewmodel.UserViewModel

class ShareListFragment : DialogFragment() {

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val fabNew = view?.findViewById<FloatingActionButton>(R.id.floatingActionButton_new_email)
        val editTextEmail = view!!.findViewById<AutoCompleteTextView>(R.id.textInputEditText_email)

        val toolbar = view?.findViewById<Toolbar>(R.id.text_view_new_product_label)
        toolbar!!.inflateMenu(R.menu.shared_menu)
        setHasOptionsMenu(true)

        fabNew!!.setOnClickListener({
            val shopping : Shopping = arguments?.get(ConstantsUtils.SHOPPING.name) as Shopping
            shopping.add(user)
            FirebaseRepository().addUserInShopping(user)

        })

        ViewModelProvider(this).get(UserViewModel::class.java).getUsers().observe(activity!!, Observer {
            val adapter = ArrayAdapter(
                activity!!,
                android.R.layout.simple_dropdown_item_1line, it
            )

            editTextEmail.setAdapter(adapter)

            editTextEmail.onItemClickListener = AdapterView.OnItemClickListener{ parent,view,position, id ->
                user = parent.getItemAtPosition(position) as User
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            dialog.getWindow()!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    companion object {

        fun newInstance(id: String, shopping: Shopping): ShareListFragment {
            val args = Bundle()
            args.putString(ConstantsUtils.ID.name, id)
            args.putSerializable(ConstantsUtils.SHOPPING.name, shopping)
            val fragment =
                ShareListFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
