package com.students.instantcrime.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class Report(
    @DocumentId
    var id: String? = null,
    var title: String? = null,
    var userId: String? = null,
    var description: String? = null,
    var location: String? = null,
    var reportImageUri: String? = null,
    var status: String? = null,
    @ServerTimestamp
    private var createAt: Date? = null
) : Serializable