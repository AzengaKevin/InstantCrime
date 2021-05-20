package com.students.instantcrime.data.enums

import java.lang.IllegalArgumentException

enum class ReportStatus(private val slug: String) {

    Default("default"), Confirmed("confirmed"), Closed("closed");

    override fun toString(): String {
        return slug;
    }

    companion object {
        fun fromString(slug: String): ReportStatus {
            return when (slug) {
                "default" -> Default
                "confirmed" -> Confirmed
                "closed" -> Closed
                else -> throw IllegalArgumentException("No such status")
            }
        }
    }
}