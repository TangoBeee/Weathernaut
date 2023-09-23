package me.tangobee.weathernaut.util

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation

class NavigateFragmentUtil {
    fun navigateToFragment(view: View, id: Int) {
        Navigation.findNavController(view).navigate(id)
    }

    fun navigateToFragmentWithAction(view: View, actions: NavDirections) {
        Navigation.findNavController(view).navigate(actions)
    }
}