package com.students.instantcrime.data.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.students.instantcrime.data.Constants
import com.students.instantcrime.data.models.Report

class ReportRepository(private val listener: ReportTaskListener?) {

    fun findAll() {
        Firebase.firestore.collection(Constants.REPORTS_ROOT)
            .addSnapshotListener { value, error ->

                if (error != null) {

                    listener?.onError(error)

                    return@addSnapshotListener
                }

                value?.let {
                    listener?.onReportsRetrieved(it.toObjects(Report::class.java))
                }

            }
    }

    fun findByUser(uid: String) {
        Firebase.firestore.collection(Constants.REPORTS_ROOT)
            .whereEqualTo("userId", uid)
            .addSnapshotListener { value, error ->

                if (error != null) {

                    listener?.onError(error)

                    return@addSnapshotListener
                }

                value?.let {
                    listener?.onReportsRetrieved(it.toObjects(Report::class.java))
                }

            }

    }

    fun findById(id: String) {
        Firebase.firestore.collection(Constants.REPORTS_ROOT)
            .document(id)
            .addSnapshotListener { value, error ->

                if (error != null) {

                    listener?.onError(error)

                    return@addSnapshotListener
                }

                value?.let {

                    if (it.exists()) {

                        listener?.onReportRetrieved(it.toObject(Report::class.java)!!)
                    }
                }

            }


    }


    interface ReportTaskListener {

        fun onReportsRetrieved(reports: List<Report>)

        fun onReportRetrieved(report: Report)

        fun onError(exception: Exception)
    }
}