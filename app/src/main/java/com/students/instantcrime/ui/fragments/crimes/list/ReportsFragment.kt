package com.students.instantcrime.ui.fragments.crimes.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.students.instantcrime.databinding.FragmentReportsBinding
import com.students.instantcrime.ui.fragments.crimes.ReportsAdapter

private const val TAG = "AllReportsFragment"

class AllReportsFragment : Fragment() {

    private lateinit var binding: FragmentReportsBinding
    private lateinit var viewModel: ReportsViewModel

    private val reportsAdapter by lazy { ReportsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(ReportsViewModel::class.java)

        viewModel.reports.observe(viewLifecycleOwner, Observer { reports ->
            reportsAdapter.reportList = reports
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.reportsContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.reportsContainer.setHasFixedSize(true)
        binding.reportsContainer.adapter = reportsAdapter
        binding.reportsContainer.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }


}