package com.students.instantcrime.data.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.data.Constants
import com.students.instantcrime.data.models.User

class UserRepository(private val listener: UserTaskListener?) {

    fun findAll() {
        Firebase.firestore.collection(Constants.USERS_ROOT)
            .addSnapshotListener { value, error ->

                if (error != null) {
                    listener?.onError(error)
                    return@addSnapshotListener
                }

                value?.let {
                    listener?.onUserRetrieved(it.toObjects(User::class.java))
                }

            }
    }

    interface UserTaskListener {

        fun onUserRetrieved(users: List<User>)

        fun onError(exception: Exception)
    }
}