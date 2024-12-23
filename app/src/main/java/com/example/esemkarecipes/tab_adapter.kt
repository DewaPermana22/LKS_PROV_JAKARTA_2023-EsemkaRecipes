package com.example.esemkarecipes

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class tab_adapter(fa : FragmentActivity) : FragmentStateAdapter(fa)  {
    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> Categories()
            1 -> Profile()
            else -> Categories()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}