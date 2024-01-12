package com.example.nt118_project.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.nt118_project.Fragments.AccumulatedPointsFragment
import com.example.nt118_project.Fragments.AllTransactionsFragment
import com.example.nt118_project.Fragments.Flight2Fragment
import com.example.nt118_project.Fragments.ForyouFragment
import com.example.nt118_project.Fragments.RedeemedPointsFragment
import com.example.nt118_project.Fragments.SuggestFragment

class FragmentPageHistoryPointAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager,lifecycle){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return if (position==0)
            AllTransactionsFragment()
        else if(position == 1)
            AccumulatedPointsFragment()
        else{
            RedeemedPointsFragment()
        }
    }
}