package com.students.instantcrime.ui.fragments.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.students.instantcrime.data.models.User
import com.students.instantcrime.databinding.UserViewBinding
import java.text.SimpleDateFormat
import java.util.*

class UserAdapter(private val listener: UserItemListener?) :
    RecyclerView.Adapter<UserAdapter.UserHolder>() {

    private val dateFormatter by lazy { SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH) }

    var users: List<User>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class UserHolder(val binding: UserViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            UserViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = users?.size ?: 0

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.binding.nameTextView.text = users!![position].name
        holder.binding.roleTextView.text = users!![position].role
        holder.binding.joinDateTextView.text =
            if (users!![position].joinDate != null) dateFormatter.format(users!![position].joinDate!!) else "Not Set"

        holder.binding.root.setOnClickListener {
            listener?.onClickListener(it, users!![position])
        }
    }

    interface UserItemListener {
        fun onClickListener(view: View, user: User)
    }
}