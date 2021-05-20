package com.students.instantcrime.ui.fragments.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.students.instantcrime.data.models.Report
import com.students.instantcrime.databinding.FragmentReportDetailBinding

class ReportDetailFragment : Fragment() {

    private lateinit var binding: FragmentReportDetailBinding
    private lateinit var report: Report

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        report = ReportDetailFragmentArgs.fromBundle(requireArguments()).report
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (this::report.isInitialized) {
            binding.statusTextView.text = report.status!!
            binding.descriptionTextView.text = report.description!!

            Glide.with(this)
                .load(report.reportImageUri)
                .centerCrop()
                .into(binding.reportImageContainer)
        }
    }

}