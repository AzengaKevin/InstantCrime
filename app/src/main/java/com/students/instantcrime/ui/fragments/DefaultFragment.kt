package com.students.instantcrime.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.students.instantcrime.R
import com.students.instantcrime.databinding.FragmentDefaultBinding

class DefaultFragment : Fragment() {

    private lateinit var binding: FragmentDefaultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDefaultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.reportNowButton.setOnClickListener { findNavController().navigate(R.id.action_add_crime) }

    }
}