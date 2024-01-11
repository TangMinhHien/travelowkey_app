package com.example.nt118_project.service_car

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nt118_project.Fragments.ServiceCar1_Fragment
import com.example.nt118_project.Fragments.ServiceCar2_Fragment

class Search_ServiceCar_Adapter (fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter (fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        if (position == 0)
            return ServiceCar1_Fragment()
        else
            return ServiceCar2_Fragment()
    }

    fun getHeight (position: Int): Int? {
        return if (position == 0)
            ServiceCar1_Fragment().view?.height
        else
            ServiceCar2_Fragment().view?.height
    }
}