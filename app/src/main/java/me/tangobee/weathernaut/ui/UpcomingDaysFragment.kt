package me.tangobee.weathernaut.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.tangobee.weathernaut.databinding.FragmentUpcomingDaysBinding

class UpcomingDaysFragment : Fragment() {

    private lateinit var binding: FragmentUpcomingDaysBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
    }

}