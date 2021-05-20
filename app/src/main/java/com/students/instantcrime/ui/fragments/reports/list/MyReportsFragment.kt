package com.students.instantcrime.ui.fragments.reports.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.R
import com.students.instantcrime.data.models.Report
import com.students.instantcrime.databinding.MyReportsFragmentBinding
import com.students.instantcrime.helpers.toast
import com.students.instantcrime.ui.fragments.reports.ReportsAdapter

private const val TAG = "MyReportsFragment"

class MyReportsFragment : Fragment(), ReportsAdapter.ReportItemListener {


    private lateinit var viewModel: MyReportsViewModel
    private lateinit var binding: MyReportsFragmentBinding

    private val reportsAdapter by lazy { ReportsAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MyReportsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val factory = ReportViewModelFactory(Firebase.auth.currentUser!!.uid)
        viewModel = ViewModelProvider(this, factory).get(MyReportsViewModel::class.java)
        viewModel.reports.observe(viewLifecycleOwner, Observer { reports ->
            reportsAdapter.reportList = reports
        })

        viewModel.exception.observe(viewLifecycleOwner, Observer { exception ->
            Log.e(
                TAG,
                "onActivityCreated: failed to get reports",
                exception
            )
            requireContext().toast("An error occurred while fetching reports, check the logs")
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addReportFab.setOnClickListener { findNavController().navigate(R.id.action_add_report_from_my_reports) }

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

    override fun onClick(view: View, report: Report) {

        val action = MyReportsFragmentDirections.actionViewReportDetailMine(report)

        findNavController().navigate(action)

    }

}