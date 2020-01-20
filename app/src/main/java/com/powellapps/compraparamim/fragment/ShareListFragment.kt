package com.powellapps.compraparamim.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment

import com.powellapps.compraparamim.R
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.model.Shopping
import com.powellapps.compraparamim.utils.ConstantsUtils
import com.powellapps.compraparamim.utils.Utils

/**
 * A simple [Fragment] subclass.
 */
class ShareListFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val textViewUserId = view?.findViewById<TextView>(R.id.textView_user_id)
        val shopping : Shopping = arguments!!.getSerializable(ConstantsUtils.SHOPPING.name) as Shopping
        val userId = arguments?.getString(ConstantsUtils.ID.name)
        val toolbar = view?.findViewById<Toolbar>(R.id.toolbar)
        toolbar!!.inflateMenu(R.menu.shared_menu)
        toolbar.setOnMenuItemClickListener({
            when(it.itemId){
                R.id.item_renew -> {
                    initShare(shopping, textViewUserId, userId)
                }
            }

            true
        })
        showShare(textViewUserId, shopping, userId)
        setHasOptionsMenu(true)
    }

    private fun initShare(
        shopping: Shopping,
        textViewUserId: TextView?,
        userId: String?
    ) {
        val result = generateShare()
        shopping.shareId = result.first
        showShare(textViewUserId, shopping, userId)
        if (userId != null) {
            FirebaseRepository().updateShare(shopping)
        }
    }

    private fun generateShare(): Pair<String, String> {
        val sharePassword = Utils().generateRandomPassword()
        val shareId = (Utils().generateRandomPassword() + Utils().generateId(FirebaseRepository().getUserId()))
        return Pair(shareId, sharePassword)
    }

    private fun showShare(
        textViewUserId: TextView?,
        shopping: Shopping,
        userId: String?
    ) {
        if(shopping.shareId.isEmpty()){
            initShare(shopping, textViewUserId, userId)
        }else{
            textViewUserId?.text = shopping.shareId
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
