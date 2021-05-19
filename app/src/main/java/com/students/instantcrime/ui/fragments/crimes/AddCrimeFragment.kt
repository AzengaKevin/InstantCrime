package com.students.instantcrime.ui.fragments.crimes

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.students.instantcrime.data.Constants
import com.students.instantcrime.data.enums.ReportStatus
import com.students.instantcrime.data.models.Report
import com.students.instantcrime.databinding.FragmentAddCrimeBinding
import com.students.instantcrime.helpers.toast
import java.util.*

private const val SELECT_IMAGE_RC = 22
private const val TAG = "AddCrimeFragment"

class AddCrimeFragment : Fragment() {

    private lateinit var reportImageUri: Uri
    private lateinit var binding: FragmentAddCrimeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddCrimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectImageButton.setOnClickListener { selectImageFromGallery() }

        binding.submitCrimeButton.setOnClickListener { handleSubmittingCrimeAction() }
    }

    private fun handleSubmittingCrimeAction() {

        //Upload the image then the report content
        val title = binding.titleField.text.toString()
        val description = binding.descriptionField.text.toString()
        val location = binding.locationField.text.toString()
        val status = ReportStatus.Default.toString()

        if (title.isEmpty()) {
            binding.titleField.error = "Title field is required"
            binding.titleField.requestFocus()
            return
        }

        if (description.isEmpty()) {
            binding.descriptionField.error = "Description field is required"
            binding.descriptionField.requestFocus()
            return
        }

        if (location.isEmpty()) {
            binding.locationField.error = "Location field is required"
            binding.locationField.requestFocus()
            return
        }

        //In the image view is initialized upload the image

        val report = Report(
            title = title,
            description = description,
            location = location,
            status = status,
            createAt = Date()
        )

        if (this::reportImageUri.isInitialized) {

            val filename = "${System.currentTimeMillis()}-${getFilenameFromUri(
                requireContext().contentResolver,
                reportImageUri
            )}"
            val reportFileRef = Firebase.storage.getReference("${Constants.REPORTS_ROOT}/$filename")

            reportFileRef.putFile(reportImageUri)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        reportFileRef.downloadUrl.addOnCompleteListener { fileDownloadUriTask ->
                            if (fileDownloadUriTask.isSuccessful) {

                                report.reportImageUri = fileDownloadUriTask.result.toString()

                                persistReport(report)


                            } else {
                                Log.e(
                                    TAG,
                                    "handleSubmittingCrimeAction: ",
                                    fileDownloadUriTask.exception
                                )
                                requireContext().toast("Get file Uri failed")
                            }
                        }
                    } else {

                        Log.e(TAG, "handleSubmittingCrimeAction: file upload failed", it.exception)

                    }
                }
        } else {

            persistReport(report)

        }


    }

    private fun persistReport(report: Report) {

        Firebase.firestore.collection(Constants.REPORTS_ROOT)
            .add(report)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    requireContext().toast("You report has been persisted successfully")

                    requireActivity().onBackPressed()
                } else {
                    Log.e(TAG, "persistReport: failed", it.exception)
                    requireContext().toast("Report persistence failed")
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                SELECT_IMAGE_RC -> {
                    data?.data?.let {

                        reportImageUri = it

                        Glide.with(this)
                            .load(it)
                            .centerCrop()
                            .into(binding.crimeImageContainer)
                    }
                }

                else -> super.onActivityResult(requestCode, resultCode, data)
            }

        } else {
            Log.e(TAG, "onActivityResult: failed")
        }
    }


    private fun selectImageFromGallery() {

        val selectImageIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectImageIntent.type = "image/*"

        startActivityForResult(selectImageIntent, SELECT_IMAGE_RC)
    }

    private fun getFilenameFromUri(resolver: ContentResolver, uri: Uri): String? {

        val cursor = resolver.query(uri, null, null, null, null)

        var filename: String? = null

        cursor?.let {

            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()

            filename = cursor.getString(nameIndex)

            cursor.close()
        }

        return filename
    }
}