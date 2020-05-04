package com.partymusicq.android.ui

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.partymusicq.android.R
import com.partymusicq.android.pojo.Party
import com.partymusicq.android.ui.adapter.SimpleFragmentPagerAdapter
import kotlinx.android.synthetic.main.party_activity.*

class PartyActivity: BaseActivity() {

    private lateinit var partyId : String
    private lateinit var firestore : FirebaseFirestore

    private lateinit var partyRef : DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.party_activity)

        partyId = intent.getStringExtra("partyId")
        firestore = FirebaseFirestore.getInstance()

        partyRef = firestore.collection("parties").document(partyId)
        partyRef.get()
            .addOnSuccessListener {
                val party = it.toObject(Party::class.java)
                supportActionBar?.title = "Pass Code: " + party?.passCode
            }

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