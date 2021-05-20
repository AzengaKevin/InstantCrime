package com.students.instantcrime.data.enums

enum class Role(private val slug: String) {
    Admin("admin"), Officer("officer"), Default("default");

    override fun toString(): String {
        return slug;
    }

    companion object {
        fun fromString(slug: String): Role {
            return when (slug) {
                "admin" -> Admin
                "officer" -> Officer
                "default" -> Default
                else -> throw IllegalArgumentException()
            }
        }
    }

}