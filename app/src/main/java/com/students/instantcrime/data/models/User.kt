package com.students.instantcrime.data.models

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class User(
    @DocumentId
    var id: String? = null,
    var name: String? = null,
    var role: String? = null,
    @ServerTimestamp
    var joinDate: Date? = null
)