package com.students.instantcrime.ui.fragments.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.students.instantcrime.data.models.User
import com.students.instantcrime.data.repositories.UserRepository
import kotlin.Exception

class UserListViewModel : ViewModel(), UserRepository.UserTaskListener {

    private val userRepository by lazy { UserRepository(this) }

    private var _users: MutableLiveData<List<User>> = MutableLiveData()
    private var _exception: MutableLiveData<Exception> = MutableLiveData()

    val exception: LiveData<Exception> = _exception
    val users: LiveData<List<User>> = _users

    init {
        userRepository.findAll()
    }


    override fun onUserRetrieved(users: List<User>) {

        _users.postValue(users)
    }

    override fun onError(exception: Exception) {

        _exception.postValue(exception)
    }
}