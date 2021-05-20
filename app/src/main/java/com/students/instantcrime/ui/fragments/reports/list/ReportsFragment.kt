package com.students.instantcrime.ui.fragments.reports.list

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
import com.students.instantcrime.data.enums.ReportStatus
import com.students.instantcrime.data.models.Report
import com.students.instantcrime.databinding.FragmentReportsBinding
import com.students.instantcrime.helpers.toast
import com.students.instantcrime.ui.fragments.reports.ReportsAdapter

private const val TAG = "AllReportsFragment"

class AllReportsFragment : Fragment(), ReportsAdapter.ReportItemListener {

    private lateinit var binding: FragmentReportsBinding
    private lateinit var viewModel: ReportsViewModel

    private val reportsAdapter by lazy { ReportsAdapter(this) }

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

        viewModel.exception.observe(viewLifecycleOwner, Observer {
            Log.e(TAG, "onActivityCreated: ", it)
            requireContext().toast("An error occurred, when fetching reports, check logs")
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

    override fun onClick(view: View, report: Report) {

        val popupMenu = PopupMenu(requireContext(), view)

        popupMenu.menuInflater.inflate(R.menu.report_status_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.confirmed_item -> {
                    changeReportStatus(report, ReportStatus.Confirmed)
                    true
                }

                R.id.closed_item -> {
                    changeReportStatus(report, ReportStatus.Closed)
                    true
                }

                R.id.default_item -> {
                    changeReportStatus(report, ReportStatus.Default)
                    true
                }

                else -> {
                    Log.d(
                        TAG,
                        "onClickListener: Nothing was selected"
                    )
                    true
                }
            }

        }

        popupMenu.show()

    }

    private fun changeReportStatus(report: Report, status: ReportStatus) {

        if (report.status!!.equals(status.toString())) {
            requireContext().toast("The report is already ${status}")
            return
        }

        Firebase.firestore.collection(Constants.REPORTS_ROOT)
            .document(report.id!!)
            .update("status", status.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    requireContext().toast("${report.title} status changed to $status")
                } else {
                    Log.e(TAG, "chageReportStatus: ", it.exception)
                    requireContext().toast("Operation failed, check the logs")
                }
            }

    }


}