package com.powellapps.buyforme.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.powellapps.buyforme.R
import com.powellapps.buyforme.model.Shopping
import com.powellapps.buyforme.repository.FirebaseRepository

class SearchListFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val editTextId = view?.findViewById<TextInputEditText>(R.id.editText_id)
        val buttonFollow = view?.findViewById<Button>(R.id.button_follow)
        buttonFollow!!.setOnClickListener({

            val id = editTextId!!.text.toString()

            FirebaseRepository().getSharedShopping(id).addSnapshotListener{ snap, e ->
                if(!snap?.isEmpty!!){
                    val shopping = snap.toObjects(Shopping::class.java).get(0)
                   // FirebaseRepository().follow(FirebaseRepository().getUserId(), shopping)
                }
            }
            buttonFollow.isEnabled = false
            dismiss()
        })
    }
}