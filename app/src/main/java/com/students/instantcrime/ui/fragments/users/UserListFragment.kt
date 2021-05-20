package com.students.instantcrime.ui.fragments.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.R
import com.students.instantcrime.data.Constants
import com.students.instantcrime.data.enums.Role
import com.students.instantcrime.data.models.User
import com.students.instantcrime.databinding.UserListFragmentBinding
import com.students.instantcrime.helpers.toast

private const val TAG = "UserListFrag"

class UserListFragment : Fragment(), UserAdapter.UserItemListener {

    private lateinit var viewModel: UserListViewModel
    private lateinit var binding: UserListFragmentBinding

    private val userAdapter by lazy { UserAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserListViewModel::class.java)


        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            userAdapter.users = users
        })

        viewModel.exception.observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "onActivityCreated: ", it)
            requireContext().toast("An error occurred, when fetching users, check logs")
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.usersContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.usersContainer.setHasFixedSize(true)
        binding.usersContainer.adapter = userAdapter
        binding.usersContainer.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

    override fun onClickListener(view: View, user: User) {

        val popupMenu = PopupMenu(requireContext(), view)

        popupMenu.menuInflater.inflate(R.menu.user_roles_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.admin_item -> {
                    changeUserRole(user, Role.Admin)
                    true
                }

                R.id.officer_item -> {
                    changeUserRole(user, Role.Officer)
                    true
                }

                R.id.default_item -> {
                    changeUserRole(user, Role.Default)
                    true
                }

                else -> {
                    Log.d(TAG, "onClickListener: Nothing was selected")
                    true
                }
            }

        }

        popupMenu.show()
    }

    private fun changeUserRole(user: User, role: Role) {

        if (user.role!!.equals(role.toString())) {
            requireContext().toast("${role} is the current user role")
            return
        }

        Firebase.firestore.collection(Constants.USERS_ROOT)
            .document(user.id!!)
            .update("role", role.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    requireContext().toast("${user.name} is now a ${role}")
                } else {
                    Log.e(TAG, "changeUserRole: failed", it.exception)
                    requireContext().toast("Operation failed check the logs")
                }
            }

    }

}