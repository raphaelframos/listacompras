package com.powellapps.compraparamim.ui.newlist


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.utils.Utils

/**
 * A simple [Fragment] subclass.
 */
class ShareListFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val textViewPassword = view?.findViewById<TextView>(R.id.textView_password)
        textViewPassword?.text = Utils().generateRandomPassword()
    }


}