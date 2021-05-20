package com.students.instantcrime.ui.fragments.users

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.students.instantcrime.databinding.UserListFragmentBinding
import com.students.instantcrime.helpers.toast

private const val TAG = "UserListFrag"

class UserListFragment : Fragment() {

    private lateinit var viewModel: UserListViewModel
    private lateinit var binding: UserListFragmentBinding

    private val userAdapter by lazy { UserAdapter() }

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

}