package com.students.instantcrime.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.students.instantcrime.R
import com.students.instantcrime.data.models.Report
import com.students.instantcrime.databinding.FragmentDefaultBinding
import com.students.instantcrime.helpers.toast
import com.students.instantcrime.ui.fragments.reports.ReportsAdapter

private const val TAG = "DefaultFragment"

class DefaultFragment : Fragment(), ReportsAdapter.ReportItemListener {

    private lateinit var binding: FragmentDefaultBinding
    private lateinit var viewModel: DefaultViewModel

    private val reportsAdapter by lazy { ReportsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDefaultBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.addReportFab.setOnClickListener { findNavController().navigate(R.id.action_add_report) }

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

        viewModel.reports.observe(viewLifecycleOwner, Observer { reports ->
            reportsAdapter.reportList = reports
        })

        viewModel.exception.observe(viewLifecycleOwner, Observer { exception ->
            Log.e(TAG, "onActivityCreated: failed to get reports", exception)
            requireContext().toast("An error occurred while fetching reports, chek the logs")
        })
    }

    override fun onClick(view: View, report: Report) {

        val action = DefaultFragmentDirections.actionViewReportDetail(report)

        findNavController().navigate(action)

    }
}