package com.students.instantcrime.ui.fragments.crimes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.students.instantcrime.data.models.Report
import com.students.instantcrime.databinding.ReportViewBinding

class ReportsAdapter : RecyclerView.Adapter<ReportsAdapter.ReportHolder>() {

    var reportList: List<Report>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ReportHolder(val binding: ReportViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
        return ReportHolder(
            ReportViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = reportList?.size ?: 0

    override fun onBindViewHolder(holder: ReportHolder, position: Int) {
        holder.binding.titleTextView.text = reportList!![position].title
        holder.binding.descriptionTextView.text = reportList!![position].description
    }
}