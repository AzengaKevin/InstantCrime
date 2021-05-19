package com.students.instantcrime.ui.fragments.crimes.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.students.instantcrime.data.models.Report
import com.students.instantcrime.data.repositories.ReportRepository
import java.lang.Exception

class MyReportsViewModel(uid: String) : ViewModel(),
    ReportRepository.ReportTaskListener {

    private val repository by lazy { ReportRepository(this) }

    private var _reports: MutableLiveData<List<Report>> = MutableLiveData()
    private var _report: MutableLiveData<Report> = MutableLiveData()
    private var _exception: MutableLiveData<Exception> = MutableLiveData()

    val reports: LiveData<List<Report>> = _reports
    val report: LiveData<Report> = _report
    val exception: LiveData<Exception> = _exception

    init {

        repository.findByUser(uid)
    }

    override fun onReportsRetrieved(reports: List<Report>) {
        _reports.postValue(reports)
    }

    override fun onReportRetrieved(report: Report) {
        _report.postValue(report)
    }

    override fun onError(exception: Exception) {
        _exception.postValue(exception)
    }

}