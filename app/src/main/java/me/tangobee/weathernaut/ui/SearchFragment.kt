package me.tangobee.weathernaut.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import me.tangobee.weathernaut.R
import me.tangobee.weathernaut.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var mainContainerView: ScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainContainerView = requireActivity().findViewById(R.id.mainContainerView)
        mainContainerView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        binding.cancelSearch.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainContainerView.background = ContextCompat.getDrawable(requireContext(), R.drawable.home_background)
    }
}