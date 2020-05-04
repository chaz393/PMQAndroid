package com.partymusicq.android.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.partymusicq.android.ui.fragments.EmptyFragment
import com.partymusicq.android.ui.fragments.PlayerFragment

class SimpleFragmentPagerAdapter(val context: Context,
                                 fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment {
        return when(p0){
            //TODO: set these to the other fragments when they get made
            0 -> EmptyFragment()
            1 -> PlayerFragment.newInstance()
            else -> EmptyFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            //TODO: also set these to correct titles
            0 -> "test"
            1 -> "Player"
            else -> "test3"
        }
    }
}