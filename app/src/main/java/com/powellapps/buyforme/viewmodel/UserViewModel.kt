package com.powellapps.buyforme.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.powellapps.buyforme.SHARED
import com.powellapps.buyforme.model.User
import com.powellapps.buyforme.repository.FirebaseRepository
import com.powellapps.buyforme.repository.NAME

class UserViewModel : ViewModel() {

    private var users = MutableLiveData<List<User>>()
    private var _shareds = MutableLiveData<List<User>>()

    fun getUsers(): MutableLiveData<List<User>> {
        FirebaseRepository().getUsers().orderBy(NAME).addSnapshotListener { it, _ ->
            it?.let {
                users.value = it.toObjects(User::class.java)
            }
        }
        return users
    }

    fun getShareds(): LiveData<List<User>> {
        FirebaseRepository().getSharedsByUser().addSnapshotListener { it, _ ->
            it?.let {
                _shareds.value = it.toObjects(User::class.java)
            }
        }
        return _shareds
    }

    fun shared(user: User, selectedTabPosition: Int) {
        if(selectedTabPosition == SHARED){
            FirebaseRepository().getShared(user).delete()
        }else{
            FirebaseRepository().updateShare().document(user.id).set(user)
        }
    }
}