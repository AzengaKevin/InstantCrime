package com.students.instantcrime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.students.instantcrime.R
import com.students.instantcrime.databinding.FragmentDefaultBinding
import com.students.instantcrime.ui.fragments.crimes.ReportsAdapter

class DefaultFragment : Fragment() {

    private lateinit var binding: FragmentDefaultBinding
    private lateinit var viewModel: DefaultViewModel

    private val reportsAdapter by lazy { ReportsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.addReportFab.setOnClickListener { findNavController().navigate(R.id.action_add_crime) }

        //Setup the recycler view

        binding.reportsContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.reportsContainer.setHasFixedSize(true)

        binding.reportsContainer.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.reportsContainer.adapter = reportsAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DefaultViewModel::class.java)
    }
}