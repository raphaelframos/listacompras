package com.powellapps.compraparamim.ui.newlist

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment

import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.ui.mylist.Shopping
import com.powellapps.compraparamim.utils.ConstantsUtils
import com.powellapps.compraparamim.utils.Utils
import kotlinx.android.synthetic.main.fragment_share_list.*
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 */
class ShareListFragment : DialogFragment() {

    lateinit var textViewPassword : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textViewPassword = view?.findViewById(R.id.textView_password)!!
        val textViewUserId = view?.findViewById<TextView>(R.id.textView_user_id)
        val shopping : Shopping = arguments!!.getSerializable(ConstantsUtils.SHOPPING.name) as Shopping
        val userId = arguments?.getString(ConstantsUtils.ID.name)
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        toolbar!!.inflateMenu(R.menu.shared_menu)
        toolbar.setOnMenuItemClickListener({
            when(it.itemId){
                R.id.item_renew -> {
                    val sharePassword = Utils().generateRandomPassword()
                    val shareId = Utils().generateId(FirebaseRepository().getUserId())
                    shopping.shareId = shareId
                    shopping.sharePassword = sharePassword
                    showShare(textViewUserId, shopping)
                    if (userId != null) {
                        FirebaseRepository().updateShare(userId, shopping)
                    }
                }
            }

            true
        })
        showShare(textViewUserId, shopping)
        setHasOptionsMenu(true)
    }

    private fun showShare(
        textViewUserId: TextView?,
        shopping: Shopping
    ) {
        textViewUserId?.text = shopping.shareId
        textViewPassword.text = shopping.sharePassword
    }

    companion object {

        fun newInstance(id: String, shopping: Shopping): ShareListFragment {
            val args = Bundle()
            args.putString(ConstantsUtils.ID.name, id)
            args.putSerializable(ConstantsUtils.SHOPPING.name, shopping)
            val fragment = ShareListFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
