package com.powellapps.buyforme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.powellapps.buyforme.PersonActivity
import com.powellapps.buyforme.databinding.AdapterPersonBinding
import com.powellapps.buyforme.model.User
import com.powellapps.buyforme.utils.Utils

class PersonAdapter(private var users: List<User>, val listener: PersonListener) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PersonViewHolder(
        AdapterPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            listener.update(user)
        }
    }

    override fun getItemCount() = users.size

    fun update(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    class PersonViewHolder(private val binding : AdapterPersonBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(user: User) {
            binding.textViewName.text = user.name
            Utils().setImage(binding, user.picture)
        }
    }

    interface PersonListener{
        fun update(user: User)
    }
}