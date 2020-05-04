package com.partymusicq.android.ui

import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.partymusicq.android.R
import com.partymusicq.android.ui.adapter.SimpleFragmentPagerAdapter
import kotlinx.android.synthetic.main.party_activity.*

class PartyActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.party_activity)

        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        viewPager.adapter = SimpleFragmentPagerAdapter(this, supportFragmentManager)
        val navigation = findViewById<TabLayout>(R.id.navigation)
        navigation.setupWithViewPager(viewPager)
        viewpager.currentItem = 1
    }

    companion object {
        const val TAG = "PartyActivity"
    }


}